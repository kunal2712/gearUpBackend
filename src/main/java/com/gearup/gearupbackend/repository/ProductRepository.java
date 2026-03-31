package com.gearup.gearupbackend.repository;

import com.gearup.gearupbackend.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product , Long> {

    List<Product> findAll();
    List<Product> findByCategoryId(Long categoryId);

    List<Product> findByNameContainingIgnoreCase(String name);
}
