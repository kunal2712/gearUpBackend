package com.gearup.gearupbackend.kafka;

import com.gearup.gearupbackend.event.PaymentSuccessEvent;
import com.gearup.gearupbackend.model.Inventory;
import com.gearup.gearupbackend.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentSuccessConsumer {

    private final InventoryService inventoryService;

    @KafkaListener(
            topics = "${app.kafka.topic.payment-success}",
            groupId = "payment-success-group"
    )
    public  void consume(PaymentSuccessEvent paymentSuccessEvent){
        log.info("Recieved Payment for order id : " + paymentSuccessEvent.getOrderId());
        inventoryService.confirmReservedStock(paymentSuccessEvent);
    }

}
