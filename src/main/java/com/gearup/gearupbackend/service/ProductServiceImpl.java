package com.gearup.gearupbackend.service;




import com.gearup.gearupbackend.model.Product;
import com.gearup.gearupbackend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public Optional<Product> getProductById(long id) {
        return productRepository.findById(id);
    }

    @Override
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(long id, Product product) {
        Optional<Product> existingProduct = productRepository.findById(id);
        if (existingProduct.isPresent()) {
            Product updatedProduct = existingProduct.get();
            updatedProduct.setCategory(product.getCategory());
            updatedProduct.setName(product.getName());
            updatedProduct.setDescription(product.getDescription());
            updatedProduct.setPrice(product.getPrice());
            updatedProduct.setImageUrl(product.getImageUrl());
            updatedProduct.setStockQuantity(product.getStockQuantity());
            return productRepository.save(updatedProduct);
        }
        return null;
    }

    @Override
    public void deleteProduct(long id) {
        productRepository.deleteById(id);
    }

    @Override
    public List<Product> getProductsByCategory(long categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }

    @Override
    public List<Product> searchProductsByName(String name) {
        return productRepository.findByNameContainingIgnoreCase(name);
    }
}
