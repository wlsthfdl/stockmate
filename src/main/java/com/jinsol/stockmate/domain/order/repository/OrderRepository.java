package com.jinsol.stockmate.domain.order.repository;

import com.jinsol.stockmate.domain.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

    java.util.List<Order> findByUserId(Long userId);
}