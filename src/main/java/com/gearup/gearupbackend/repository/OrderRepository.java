package com.gearup.gearupbackend.repository;

import com.gearup.gearupbackend.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {}
