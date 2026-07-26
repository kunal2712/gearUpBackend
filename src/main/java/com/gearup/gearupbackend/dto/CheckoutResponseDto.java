package com.gearup.gearupbackend.dto;

import com.gearup.gearupbackend.model.enums.OrderStatus;
import com.gearup.gearupbackend.model.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutResponseDto {

    private Long orderId;

    private BigDecimal totalAmount;

    private OrderStatus orderStatus;

    private PaymentStatus paymentStatus;

    private String message;
}