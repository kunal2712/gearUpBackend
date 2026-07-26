package com.gearup.gearupbackend.kafka;

import com.gearup.gearupbackend.event.OrderCreatedEvent;
import com.gearup.gearupbackend.model.Order;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderEventProducer {

    private static final String TOPIC = "order-created";

    private final KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate;

    public  void orderPublishEvent(OrderCreatedEvent orderEvent){
        log.info("Publishing Order event for order id : " + orderEvent.getOrderId());

        kafkaTemplate.send(TOPIC , orderEvent);

        log.info("Order published successfully!");
    }
}
