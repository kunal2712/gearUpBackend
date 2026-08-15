package com.gearup.gearupbackend.kafka;

import com.gearup.gearupbackend.event.PaymentSuccessEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentEventProducer {

    private final KafkaTemplate<String , PaymentSuccessEvent> paymentSuccessEventKafkaTemplate;
    private static final String TOPIC = "payment-successful";


    public void publishPaymentSuccess(PaymentSuccessEvent paymentSuccessEvent) {
        log.info("Payment published successfully for order id : {}", paymentSuccessEvent.getOrderId());

        paymentSuccessEventKafkaTemplate.send(TOPIC ,String.valueOf(paymentSuccessEvent.getOrderId()),  paymentSuccessEvent);

        log.info("Payment published successfully with transaction id : {}", paymentSuccessEvent.getTransactionId());
    }

}
