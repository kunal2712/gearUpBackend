package com.gearup.gearupbackend.kafka;

import com.gearup.gearupbackend.event.OrderCreatedEvent;
import com.gearup.gearupbackend.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryConsumer {

    private final InventoryService inventoryService;

    @KafkaListener(
            topics = "${app.kafka.topic.order-created}",
            groupId = "inventory-group"
    )
    public void consume(OrderCreatedEvent event) {

        log.info("Received OrderCreatedEvent : {}", event.getOrderId());

        inventoryService.processOrder(event);
    }
}