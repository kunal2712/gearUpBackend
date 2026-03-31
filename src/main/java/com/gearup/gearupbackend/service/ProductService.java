package com.gearup.gearupbackend.service;

import com.gearup.gearupbackend.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {


    Optional<Product> getProductById(long id);

    Product createProduct(Product product);

    Product updateProduct(long id , Product product);

    void deleteProduct(long id);

    List<Product> getProductsByCategory(long categoryId);

    List<Product> searchProductsByName(String name);

    List<Product> findAll();
}
