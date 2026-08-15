package com.gearup.gearupbackend.service;


import com.gearup.gearupbackend.event.PaymentSuccessEvent;
import com.gearup.gearupbackend.kafka.PaymentEventProducer;
import com.gearup.gearupbackend.model.Order;
import com.gearup.gearupbackend.model.Payment;
import com.gearup.gearupbackend.model.enums.OrderStatus;
import com.gearup.gearupbackend.model.enums.PaymentStatus;
import com.gearup.gearupbackend.repository.OrderRepository;
import com.gearup.gearupbackend.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;


import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private  final PaymentEventProducer paymentEventProducer;

    @Override
    @Transactional
    public void completePayment(Long orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new RuntimeException("Order not found : " + orderId));



        Payment payment = paymentRepository.findByOrder(order)
                .orElseThrow(() ->
                        new RuntimeException("Payment not found for Order : " + orderId));

        if(payment.getPaymentStatus().equals(PaymentStatus.SUCCESS)){
            throw new RuntimeException("Payment already done for this order.");
        }
        order.setOrderStatus(OrderStatus.CONFIRMED);
        order.setPaymentStatus(PaymentStatus.SUCCESS);
        payment.setPaymentStatus(PaymentStatus.SUCCESS);
        payment.setTransactionId(UUID.randomUUID().toString());

        orderRepository.save(order);
        paymentRepository.save(payment);


        log.info("""
            Payment Successful
            Order Id       : {}
            Transaction Id : {}
            """,
                order.getId(),
                payment.getTransactionId());

        PaymentSuccessEvent paymentSuccessEvent = new PaymentSuccessEvent();
        paymentSuccessEvent.setOrderId(order.getId());
        paymentSuccessEvent.setTransactionId(payment.getTransactionId());

        paymentEventProducer.publishPaymentSuccess(paymentSuccessEvent);

        log.info("Payment event published successfully");

    }
}