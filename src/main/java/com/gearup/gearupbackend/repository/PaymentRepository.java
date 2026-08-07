package com.gearup.gearupbackend.repository;

import com.gearup.gearupbackend.model.Order;
import com.gearup.gearupbackend.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {


    Optional<Payment> findByOrder(Order order);
}