package com.gearup.gearupbackend.repository;

import com.gearup.gearupbackend.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {}