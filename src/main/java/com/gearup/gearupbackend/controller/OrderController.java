package com.gearup.gearupbackend.controller;


import com.gearup.gearupbackend.dto.CheckoutRequestDto;
import com.gearup.gearupbackend.dto.CheckoutResponseDto;
import com.gearup.gearupbackend.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/gearup/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/checkout")
    public ResponseEntity<CheckoutResponseDto> checkout(
            @Valid @RequestBody CheckoutRequestDto request) {

        CheckoutResponseDto response = orderService.checkout(request);


        return ResponseEntity.ok(response);
    }
}