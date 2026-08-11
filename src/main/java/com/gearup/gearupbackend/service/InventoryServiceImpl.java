package com.gearup.gearupbackend.service;

import com.gearup.gearupbackend.event.OrderCreatedEvent;
import com.gearup.gearupbackend.event.PaymentSuccessEvent;
import com.gearup.gearupbackend.model.Inventory;
import com.gearup.gearupbackend.model.Order;
import com.gearup.gearupbackend.model.OrderItem;
import com.gearup.gearupbackend.model.Product;
import com.gearup.gearupbackend.repository.InventoryRepository;
import com.gearup.gearupbackend.repository.OrderRepository;
import com.gearup.gearupbackend.repository.ProductRepository;
import com.gearup.gearupbackend.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;


    @Override
    @Transactional
    public void processOrder(OrderCreatedEvent event) {

        log.info("Processing inventory for Order {}", event.getOrderId());

        for (Map.Entry<Long, Integer> entry : event.getProducts().entrySet()) {

            Long productId = entry.getKey();
            Integer orderedQuantity = entry.getValue();

            log.info("Product ID : {} | Ordered Quantity : {}", productId, orderedQuantity);

            Inventory inventory = inventoryRepository.findByProductId(productId)
                    .orElseThrow(() -> new RuntimeException(
                            "Inventory not found for product id : " + productId));

            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException(
                            "Product not found : " + productId));

            Integer availableBefore = inventory.getAvailableQuantity();
            Integer reservedBefore = inventory.getReservedQuantity();
            Integer productBefore = product.getStockQuantity();

            log.info("Current Available Stock : {}", availableBefore);

            if (availableBefore < orderedQuantity) {
                throw new RuntimeException(
                        "Insufficient stock for product : " + product.getName());
            }

            // Update Inventory
            inventory.setAvailableQuantity(availableBefore - orderedQuantity);
            inventory.setReservedQuantity(reservedBefore + orderedQuantity);

            // Update Product Stock
            product.setStockQuantity(productBefore - orderedQuantity);

            // Save Changes
            inventoryRepository.save(inventory);
            productRepository.save(product);

            log.info("""
                Inventory Updated Successfully
                Product ID        : {}
                Ordered Quantity  : {}
                Available Before  : {}
                Available After   : {}
                Reserved Before   : {}
                Reserved After    : {}
                Product Before    : {}
                Product After     : {}
                """,
                    productId,
                    orderedQuantity,
                    availableBefore,
                    inventory.getAvailableQuantity(),
                    reservedBefore,
                    inventory.getReservedQuantity(),
                    productBefore,
                    product.getStockQuantity());
        }

        log.info("Inventory processing completed for Order {}", event.getOrderId());
    }

    @Override
    @Transactional
    public void confirmReservedStock(PaymentSuccessEvent event) {

        log.info("Inside confirmReservedStock for Order : {}", event.getOrderId());

        Order order = orderRepository.findById(event.getOrderId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Order not found for id : " + event.getOrderId()
                        ));

        List<OrderItem> orderItems = order.getOrderItems();

        for (OrderItem item : orderItems) {

            Product product = item.getProduct();

            Inventory inventory = inventoryRepository.findByProductId(product.getId())
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Inventory not found for product id : " + product.getId()
                            ));

            int reservedBefore = inventory.getReservedQuantity();
            int orderedQuantity = item.getQuantity();

            if (reservedBefore < orderedQuantity) {
                throw new RuntimeException(
                        "Insufficient reserved quantity for product id : "
                                + product.getId()
                );
            }

            inventory.setReservedQuantity(
                    reservedBefore - orderedQuantity
            );

            inventoryRepository.save(inventory);

            log.info("""
                Inventory Reservation Confirmed
                Order ID        : {}
                Product ID      : {}
                Ordered Quantity: {}
                Reserved Before : {}
                Reserved After  : {}
                """,
                    order.getId(),
                    product.getId(),
                    orderedQuantity,
                    reservedBefore,
                    inventory.getReservedQuantity()
            );
        }

        log.info(
                "Inventory reservation confirmed successfully for Order : {}",
                event.getOrderId()
        );
    }
}