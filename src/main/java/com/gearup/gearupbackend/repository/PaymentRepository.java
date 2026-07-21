package com.gearup.gearupbackend.repository;

import com.gearup.gearupbackend.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {}