package com.gearup.gearupbackend.service;


import com.gearup.gearupbackend.config.AuthenticationFacade;
import com.gearup.gearupbackend.dto.CheckoutRequestDto;
import com.gearup.gearupbackend.dto.CheckoutResponseDto;
import com.gearup.gearupbackend.event.OrderCreatedEvent;
import com.gearup.gearupbackend.kafka.OrderEventProducer;
import com.gearup.gearupbackend.model.*;
import com.gearup.gearupbackend.model.enums.OrderStatus;
import com.gearup.gearupbackend.model.enums.PaymentStatus;
import com.gearup.gearupbackend.repository.CartRepository;
import com.gearup.gearupbackend.repository.OrderRepository;
import com.gearup.gearupbackend.repository.PaymentRepository;
import com.gearup.gearupbackend.service.CartService;
import com.gearup.gearupbackend.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final CartRepository cartRepository;
    private final CartService cartService;
    private final AuthenticationFacade authenticationFacade;
    private final OrderEventProducer orderEventProducer;

    @Override
    public CheckoutResponseDto checkout(CheckoutRequestDto request) {

        User currentUser = authenticationFacade.getCurrentUser();

        Cart cart = getUserCart(currentUser);

        validateCart(cart);

        Order order = createOrder(currentUser , request);


        BigDecimal totalAmount = createOrderItems(order, cart);

        order.setTotalAmount(totalAmount);

        Payment payment = createPayment(order);

        order.addPayment(payment);

        Order savedOrder = orderRepository.save(order);

        OrderCreatedEvent event = buildOrderCreatedEvent(savedOrder);

        orderEventProducer.orderPublishEvent(event);

        cartService.clearCart(cart);


        return buildResponse(savedOrder);
    }

    private Cart getUserCart(User user) {

        return cartRepository.findByUser(user)
                .orElseThrow(() ->
                        new RuntimeException("Cart not found for user."));
    }

    private void validateCart(Cart cart){
        if(cart.getCartItems() == null || cart.getCartItems().isEmpty()){
            throw new RuntimeException("Cart is empty!");
        }

    }

    private Order createOrder(User user, CheckoutRequestDto request) {

        return Order.builder()
                .user(user)
                .orderStatus(OrderStatus.PENDING)
                .paymentStatus(PaymentStatus.PENDING)
                .paymentMethod(request.getPaymentMethod())
                .shippingAddress(request.getShippingAddress())
                .city(request.getCity())
                .state(request.getState())
                .pincode(request.getPincode())
                .build();
    }

    private BigDecimal createOrderItems(Order order, Cart cart) {

        BigDecimal totalAmount = BigDecimal.ZERO;

        for (CartItem cartItem : cart.getCartItems()) {

            Product product = cartItem.getProduct();

            BigDecimal subtotal =
                    product.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()));

            OrderItem orderItem = OrderItem.builder()
                    .product(product)
                    .quantity(cartItem.getQuantity())
                    .priceAtPurchase(product.getPrice())
                    .subtotal(subtotal)
                    .build();

            order.addOrderItem(orderItem);

            totalAmount = totalAmount.add(subtotal);
        }

        return totalAmount;
    }

    private Payment createPayment(Order order) {

        return Payment.builder()
                .amount(order.getTotalAmount())
                .paymentMethod(order.getPaymentMethod())
                .paymentStatus(PaymentStatus.PENDING)
                .transactionId(UUID.randomUUID().toString())
                .build();
    }

    private CheckoutResponseDto buildResponse(Order order) {

        return CheckoutResponseDto.builder()
                .orderId(order.getId())
                .totalAmount(order.getTotalAmount())
                .orderStatus(order.getOrderStatus())
                .paymentStatus(order.getPaymentStatus())
                .message("Order placed successfully.")
                .build();
    }

    public OrderCreatedEvent buildOrderCreatedEvent(Order order){

        Map<Long, Integer> products = order.getOrderItems()
                .stream()
                .collect(Collectors.toMap(
                        item -> item.getProduct().getId(),
                        OrderItem::getQuantity
                ));

        return OrderCreatedEvent.builder()
                .orderId(order.getId())
                .userId(order.getUser().getId())
                .totalAmount(order.getTotalAmount())
                .products(products)
                .build();
    }
}