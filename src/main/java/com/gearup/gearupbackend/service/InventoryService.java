package com.gearup.gearupbackend.service;

import com.gearup.gearupbackend.event.OrderCreatedEvent;
import com.gearup.gearupbackend.event.PaymentSuccessEvent;

public interface InventoryService {
    void processOrder(OrderCreatedEvent event);

    void confirmReservedStock(PaymentSuccessEvent event);

}
