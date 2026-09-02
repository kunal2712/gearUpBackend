package com.gearup.gearupbackend.service;




import com.gearup.gearupbackend.dto.CategoryResponseDto;
import com.gearup.gearupbackend.dto.ProductResponseDto;
import com.gearup.gearupbackend.mapper.CategoryMapper;
import com.gearup.gearupbackend.model.Product;
import com.gearup.gearupbackend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryMapper categoryMapper;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, CategoryMapper categoryMapper) {
        this.productRepository = productRepository;
        this.categoryMapper = categoryMapper;
    }

    @Override
//    @Cacheable(value = "products", key = "'all'")
    public List<ProductResponseDto> findAll() {
        System.out.println("db hit!");
         return productRepository.findAll().stream()
                 .map((product -> ProductResponseDto.builder()
                         .id(product.getId())
                         .name(product.getName())
                         .imageUrl(product.getImageUrl())
                         .price(product.getPrice())
                         .stockQuantity(product.getStockQuantity())
                         .description(product.getDescription())
                         .updatedAt(product.getUpdatedAt())
                         .createdAt(product.getCreatedAt())
                         .category(categoryMapper.toCategoryResponseDto(product.getCategory()))
                         .build()))
                        .toList();
    }

    @Override
    public Optional<Product> getProductById(long id) {
        return productRepository.findById(id);
    }

//    @Caching(evict = {
//            @CacheEvict(value = "products", key = "'all'"),
//            @CacheEvict(value = "productsByCategory", allEntries = true),
//            @CacheEvict(value = "productSearch", allEntries = true)
//    })
    @Override
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

//    @Caching(evict = {
//            @CacheEvict(value = "products", key = "'all'"),
//            @CacheEvict(value = "productsByCategory", allEntries = true),
//            @CacheEvict(value = "productSearch", allEntries = true)
//    })
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

//    @Caching(evict = {
//            @CacheEvict(value = "products", key = "'all'"),
//            @CacheEvict(value = "productsByCategory", allEntries = true),
//            @CacheEvict(value = "productSearch", allEntries = true)
//    })
    @Override
    public void deleteProduct(long id) {
        productRepository.deleteById(id);
    }

//    @Cacheable(value = "productsByCategory", key = "#categoryId")
    @Override
    public List<Product> getProductsByCategory(long categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }

//    @Cacheable(value = "productSearch", key = "#name.toLowerCase()")
    @Override
    public List<Product> searchProductsByName(String name) {
        return productRepository.findByNameContainingIgnoreCase(name);
    }
}
