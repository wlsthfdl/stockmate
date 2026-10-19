package com.jinsol.stockmate.domain.order.service;
import static org.junit.jupiter.api.Assertions.assertEquals;
import com.jinsol.stockmate.domain.category.entity.Category;
import com.jinsol.stockmate.domain.category.repository.CategoryRepository;
import com.jinsol.stockmate.domain.inventory.entity.Inventory;
import com.jinsol.stockmate.domain.inventory.repository.InventoryRepository;
import com.jinsol.stockmate.domain.order.dto.OrderCreateRequest;
import com.jinsol.stockmate.domain.order.dto.OrderItemRequest;
import com.jinsol.stockmate.domain.order.dto.OrderResponse;
import com.jinsol.stockmate.domain.order.enums.OrderStatus;
import com.jinsol.stockmate.domain.product.entity.Product;
import com.jinsol.stockmate.domain.product.enums.ProductStatus;
import com.jinsol.stockmate.domain.product.repository.ProductRepository;
import com.jinsol.stockmate.domain.user.entity.User;
import com.jinsol.stockmate.domain.user.enums.Role;
import com.jinsol.stockmate.domain.user.repository.UserRepository;
import com.jinsol.stockmate.global.exception.InsufficientStockException;
import com.jinsol.stockmate.global.exception.InvalidOrderStatusException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootTest
public class OrderConcurrencyTest {

    @Autowired private OrderService orderService;
    @Autowired private CategoryRepository categoryRepository;
    @Autowired private ProductRepository productRepository;
    @Autowired private InventoryRepository inventoryRepository;
    @Autowired private UserRepository userRepository;

    private Product product;
    private User user;

    @BeforeEach
    void setUp() {
        Category category = categoryRepository.save(
                Category.builder().name("테스트카테고리-" + System.currentTimeMillis()).build());

        product = productRepository.save(Product.builder()
                .name("한정판 포토카드")
                .description("동시성 테스트용")
                .price(10000)
                .currency("KRW")
                .status(ProductStatus.ON_SALE)
                .category(category)
                .build());

        inventoryRepository.save(Inventory.builder()
                .product(product)
                .quantity(5)   // 재고 5개
                .build());

        user = userRepository.save(User.builder()
                .email("concurrency-test-" + System.currentTimeMillis() + "@test.com")
                .password("encoded")
                .name("테스트유저")
                .role(Role.ADMIN)
                .build());
    }

    @Test
    void 재고보다_많은_동시주문이_들어와도_재고는_0이하로_내려가지_않는다() throws InterruptedException {
        int threadCount = 10; // 10명이 동시에 주문
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch startLatch = new CountDownLatch(1); // 모든 스레드를 동시에 출발시키기 위한 신호
        CountDownLatch latch = new CountDownLatch(threadCount);

        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failCount = new AtomicInteger();
        AtomicInteger unexpectedCount = new AtomicInteger(); // 재고부족 외의 예외 (데드락, 락 타임아웃 등)

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    OrderItemRequest itemRequest = new OrderItemRequest(product.getId(), 1);
                    OrderCreateRequest request = new OrderCreateRequest(List.of(itemRequest));

                    startLatch.await(); // 출발 신호까지 대기 , 동시에 출발
                    orderService.createOrder(user.getEmail(), request);
                    successCount.incrementAndGet();
                } catch (InsufficientStockException e) {
                    failCount.incrementAndGet();
                } catch (Exception e) {
                    unexpectedCount.incrementAndGet();
                    e.printStackTrace();
                } finally {
                    latch.countDown(); // 스레드 하나 끝날 때마다 카운트다운
                }
            });
        }

        startLatch.countDown(); // 10개 스레드 동시 출발
        latch.await(); // 10개 스레드 전부 끝날 때까지 대기
        executorService.shutdown();

        Inventory finalInventory = inventoryRepository.findByProductId(product.getId()).orElseThrow();

        System.out.println("성공한 주문 수: " + successCount.get());
        System.out.println("실패한 주문 수: " + failCount.get());
        System.out.println("최종 재고: " + finalInventory.getQuantity());

        assertEquals(0, unexpectedCount.get()); // 재고부족 외의 예외는 없어야 함
        assertEquals(5, successCount.get());   // 재고 5개만큼만 성공해야 함
        assertEquals(5, failCount.get());      // 나머지 5개는 재고부족으로 실패해야 함
        assertEquals(0, finalInventory.getQuantity()); // 최종 재고는 정확히 0
    }

    @Test
    void 같은_주문을_동시에_여러번_취소해도_재고는_한번만_복구된다() throws InterruptedException {
        // 준비: 수량 2짜리 주문 생성 → 재고 5 → 3
        OrderCreateRequest createRequest = new OrderCreateRequest(
                List.of(new OrderItemRequest(product.getId(), 2)));
        OrderResponse order = orderService.createOrder(user.getEmail(), createRequest);
        Long orderId = order.getId();

        int threadCount = 10; // 10개 요청이 동시에 같은 주문을 취소
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch latch = new CountDownLatch(threadCount);

        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failCount = new AtomicInteger();
        AtomicInteger unexpectedCount = new AtomicInteger();

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    startLatch.await();
                    // isAdmin=false: 본인 주문을 취소하는 USER 시나리오 (소유자 검사 경로까지 함께 검증)
                    orderService.cancelOrder(orderId, user.getEmail(), false);
                    successCount.incrementAndGet();
                } catch (InvalidOrderStatusException e) {
                    failCount.incrementAndGet(); // 이미 취소된 주문 → 409
                } catch (Exception e) {
                    unexpectedCount.incrementAndGet();
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });
        }

        startLatch.countDown();
        latch.await();
        executorService.shutdown();

        Inventory finalInventory = inventoryRepository.findByProductId(product.getId()).orElseThrow();
        OrderResponse finalOrder = orderService.getOrder(orderId, user.getEmail(), false);

        System.out.println("취소 성공 수: " + successCount.get());
        System.out.println("취소 실패 수: " + failCount.get());
        System.out.println("최종 재고: " + finalInventory.getQuantity());

        assertEquals(0, unexpectedCount.get());          // 상태 충돌 외의 예외는 없어야 함
        assertEquals(1, successCount.get());             // 취소는 딱 1번만 성공
        assertEquals(9, failCount.get());                // 나머지 9번은 이미 취소됨으로 실패
        assertEquals(5, finalInventory.getQuantity());   // 재고는 정확히 한 번만 복구 (3 + 2)
        assertEquals(OrderStatus.CANCELED, finalOrder.getStatus());
    }

}
