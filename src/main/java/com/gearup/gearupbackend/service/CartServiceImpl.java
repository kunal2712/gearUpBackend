package com.gearup.gearupbackend.service;

import com.gearup.gearupbackend.model.Cart;
import com.gearup.gearupbackend.model.CartItem;
import com.gearup.gearupbackend.model.Product;
import com.gearup.gearupbackend.model.User;
import com.gearup.gearupbackend.repository.CartRepository;
import com.gearup.gearupbackend.repository.ProductRepository;
import com.gearup.gearupbackend.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository; // Needed to fetch the User object

    public Cart addItemToCart(Long userId, Long productId, int quantity) {
        // 1. Get the Cart
        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new RuntimeException("User not found"));
                    return cartRepository.save(new Cart(user));
                });

        // 2. Check if product exists in cart using the Product object's ID
        CartItem existingItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(null);

        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
        } else {
            // 3. Fetch the actual Product entity to create a new CartItem
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            CartItem newItem = new CartItem(cart, product, quantity);
            cart.getCartItems().add(newItem);
        }

        return cartRepository.save(cart);
    }

    public void removeItemFromCart(Long userId, Long productId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        cart.getCartItems().removeIf(item -> item.getProduct().getId().equals(productId));

        cartRepository.save(cart);
    }

    @Override
    public Cart getCartByUserId(Long userId) {
        return cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
    }
}