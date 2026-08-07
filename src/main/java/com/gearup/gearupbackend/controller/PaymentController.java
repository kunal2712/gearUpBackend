package com.gearup.gearupbackend.controller;


import com.gearup.gearupbackend.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/{orderId}/success")
    public ResponseEntity<String> paymentSuccess(@PathVariable Long orderId){

        paymentService.completePayment(orderId);

        return ResponseEntity.ok("Payment completed successfully.");
    }
}