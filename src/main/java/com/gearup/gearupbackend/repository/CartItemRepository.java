package com.gearup.gearupbackend.repository;

import com.gearup.gearupbackend.model.Cart;
import com.gearup.gearupbackend.model.CartItem;
import com.gearup.gearupbackend.model.Product;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem , Long> {
    Optional<CartItem> findByCartAndProduct(Cart cart, Product product);
    List<CartItem> findByCart(Cart cart);
    @Transactional
    void deleteByCartAndProduct(Cart cart, Product product);
    @Transactional
    void deleteByCart(Cart cart);
}
