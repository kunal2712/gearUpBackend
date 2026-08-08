package com.gearup.gearupbackend.event;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentSuccessEvent {

    private long orderId;

    private String transactionId;
}
