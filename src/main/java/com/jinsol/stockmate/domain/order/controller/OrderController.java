package com.jinsol.stockmate.domain.order.controller;

import com.jinsol.stockmate.domain.order.dto.OrderCreateRequest;
import com.jinsol.stockmate.domain.order.dto.OrderResponse;
import com.jinsol.stockmate.domain.order.dto.OrderStatusUpdateRequest;
import com.jinsol.stockmate.domain.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;   // ← 추가
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")   // ← 추가
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(
            Authentication authentication,
            @Valid @RequestBody OrderCreateRequest request
    ) {
        String email = authentication.getName();
        OrderResponse response = orderService.createOrder(email, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PreAuthorize("hasRole('ADMIN')")   // ← 추가
    @PatchMapping("/{orderId}/status")
    public ResponseEntity<OrderResponse> updateOrderStatus(
            @PathVariable Long orderId,
            @Valid @RequestBody OrderStatusUpdateRequest request) {
        return ResponseEntity.ok(orderService.updateOrderStatus(orderId, request));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")   // ← 추가
    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<OrderResponse> cancelOrder(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.cancelOrder(orderId));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")   // ← 추가
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.getOrder(orderId));
    }

    @PreAuthorize("hasRole('ADMIN')")   // ← 추가
    @GetMapping
    public ResponseEntity<PagedModel<OrderResponse>> getOrders(
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(orderService.getOrders(pageable));
    }
}