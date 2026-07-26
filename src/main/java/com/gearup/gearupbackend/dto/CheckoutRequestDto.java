package com.gearup.gearupbackend.dto;


import com.gearup.gearupbackend.model.enums.PaymentMethod;
import lombok.Data;

@Data
public class CheckoutRequestDto {

    private String shippingAddress;

    private String city;

    private String state;

    private String pincode;

    private PaymentMethod paymentMethod;
}