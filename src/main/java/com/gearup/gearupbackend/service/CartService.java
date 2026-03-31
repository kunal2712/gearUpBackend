package com.gearup.gearupbackend.service;

import com.gearup.gearupbackend.model.Cart;

public interface CartService {

    public Cart addItemToCart(Long userId, Long productId, int quantity) ;
    public void removeItemFromCart(Long userId, Long productId);

    Cart getCartByUserId(Long userId);
}
