package com.jinsol.stockmate.domain.order.controller;

import com.jinsol.stockmate.domain.order.dto.OrderCreateRequest;
import com.jinsol.stockmate.domain.order.dto.OrderResponse;
import com.jinsol.stockmate.domain.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(
            Authentication authentication,
            @Valid @RequestBody OrderCreateRequest request
    ) {
        String email = authentication.getName();
        OrderResponse response = orderService.createOrder(email, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
