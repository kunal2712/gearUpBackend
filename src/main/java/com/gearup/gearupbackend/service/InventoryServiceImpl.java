package com.gearup.gearupbackend.service;

import com.gearup.gearupbackend.event.OrderCreatedEvent;
import com.gearup.gearupbackend.model.Inventory;
import com.gearup.gearupbackend.model.Product;
import com.gearup.gearupbackend.repository.InventoryRepository;
import com.gearup.gearupbackend.repository.ProductRepository;
import com.gearup.gearupbackend.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final ProductRepository productRepository;

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
}