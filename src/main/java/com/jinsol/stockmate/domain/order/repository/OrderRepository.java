package com.jinsol.stockmate.domain.order.repository;

import com.jinsol.stockmate.domain.order.entity.Order;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    java.util.List<Order> findByUserId(Long userId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Order> findWithLockById(Long orderId);
}