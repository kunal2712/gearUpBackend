package com.gearup.gearupbackend.service;

import com.gearup.gearupbackend.event.OrderCreatedEvent;
import com.gearup.gearupbackend.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class InventoryServiceImpl implements InventoryService {

    @Override
    public void processOrder(OrderCreatedEvent event) {

        log.info("Processing inventory for Order {}", event.getOrderId());

    }
}