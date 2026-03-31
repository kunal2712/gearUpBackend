package com.gearup.gearupbackend.controller;


import com.gearup.gearupbackend.model.Cart;
import com.gearup.gearupbackend.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/gearup/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/{userId}/add")
    public ResponseEntity<Cart> addToCart(
            @PathVariable Long userId,
            @RequestParam Long productId,
            @RequestParam Integer quantity) {

        Cart updatedCart = cartService.addItemToCart(userId, productId, quantity);
        return ResponseEntity.ok(updatedCart);
    }


    @DeleteMapping("/{userId}/remove/{productId}")
    public ResponseEntity<Void> removeFromCart(
            @PathVariable Long userId,
            @PathVariable Long productId) {

        cartService.removeItemFromCart(userId, productId);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/{userId}")
    public ResponseEntity<Cart> getCart(@PathVariable Long userId) {
        Cart cart = cartService.getCartByUserId(userId);
        return ResponseEntity.ok(cart);
    }
}