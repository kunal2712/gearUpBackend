package com.gearup.gearupbackend.service;

import com.gearup.gearupbackend.event.OrderCreatedEvent;

public interface InventoryService {
    void processOrder(OrderCreatedEvent event);

}
