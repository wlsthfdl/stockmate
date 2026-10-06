package com.jinsol.stockmate.domain.order.service;

import com.jinsol.stockmate.domain.inventory.entity.Inventory;
import com.jinsol.stockmate.domain.inventory.repository.InventoryRepository;
import com.jinsol.stockmate.domain.order.dto.OrderCreateRequest;
import com.jinsol.stockmate.domain.order.dto.OrderItemRequest;
import com.jinsol.stockmate.domain.order.dto.OrderResponse;
import com.jinsol.stockmate.domain.order.dto.OrderStatusUpdateRequest;
import com.jinsol.stockmate.domain.order.entity.Order;
import com.jinsol.stockmate.domain.order.entity.OrderItem;
import com.jinsol.stockmate.domain.order.enums.OrderStatus;
import com.jinsol.stockmate.domain.order.repository.OrderRepository;
import com.jinsol.stockmate.domain.product.entity.Product;
import com.jinsol.stockmate.domain.product.repository.ProductRepository;
import com.jinsol.stockmate.domain.user.entity.User;
import com.jinsol.stockmate.domain.user.repository.UserRepository;
import com.jinsol.stockmate.global.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;
    private final UserRepository userRepository;

    @Transactional
    public OrderResponse createOrder(String email, OrderCreateRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 사용자입니다."));

        Order order = Order.builder()
                .user(user)
                .status(OrderStatus.PAID)
                .totalPrice(0)
                .currency("KRW")
                .build();

        int totalPrice = 0;

        for (OrderItemRequest itemRequest : request.getItems()) {
            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 상품입니다."));

            // 재고 차감 (Entity의 decrease()가 재고 부족 검증까지 처리)
            // findByProductIdForUpdate : 트랜잭션 안에서 조회한 순간 재고 행에 락이 걸림
            Inventory inventory = inventoryRepository.findByProductIdForUpdate(product.getId())
                    .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 재고입니다."));
            inventory.decrease(itemRequest.getQuantity());

            OrderItem orderItem = OrderItem.builder()
                    .product(product)
                    .quantity(itemRequest.getQuantity())
                    .price(product.getPrice())   // 현재 가격을 스냅샷으로 저장
                    .build();

            order.addOrderItem(orderItem);   // 연관관계 편의 메서드
            totalPrice += product.getPrice() * itemRequest.getQuantity();
        }

        order.changeTotalPrice(totalPrice);

        Order savedOrder = orderRepository.save(order);
        return new OrderResponse(savedOrder);
    }

    @Transactional
    public OrderResponse updateOrderStatus(Long orderId, OrderStatusUpdateRequest request) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 주문입니다."));

        order.changeStatus(request.getStatus());   // 전이 규칙 검증은 엔티티가 처리

        return new OrderResponse(order);
    }

    @Transactional
    public OrderResponse cancelOrder(Long orderId) {
        Order order = orderRepository.findWithLockById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 주문입니다."));

        order.cancel();   // 취소 가능 상태 검증 + 상태 변경 (불가하면 409)

        // 상품 id 오름차순으로 락을 잡아 데드락 방지
        List<OrderItem> items = order.getOrderItems().stream()
                .sorted(Comparator.comparing(item -> item.getProduct().getId()))
                .toList();

        for (OrderItem item : items) {
            Inventory inventory = inventoryRepository.findByProductIdForUpdate(item.getProduct().getId())
                    .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 재고입니다."));
            inventory.increase(item.getQuantity());
        }

        return new OrderResponse(order);
    }
}
