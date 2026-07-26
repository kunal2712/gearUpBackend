package com.gearup.gearupbackend.service;


import com.gearup.gearupbackend.dto.CheckoutRequestDto;
import com.gearup.gearupbackend.dto.CheckoutResponseDto;

public interface OrderService {

    CheckoutResponseDto checkout(CheckoutRequestDto request);

}