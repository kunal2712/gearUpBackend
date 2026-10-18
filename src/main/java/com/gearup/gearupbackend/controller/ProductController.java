package com.gearup.gearupbackend.controller;


import com.gearup.gearupbackend.dto.ProductResponseDto;
import com.gearup.gearupbackend.model.Category;
import com.gearup.gearupbackend.model.Product;
import com.gearup.gearupbackend.service.ProductService;
import com.gearup.gearupbackend.service.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/gearup/products")
public class ProductController {

    private final ProductService productService;

    private final RecommendationService recommendationService;


    @Autowired
    public ProductController(ProductService productService, RecommendationService recommendationService) {
        this.productService = productService;
        this.recommendationService = recommendationService;
    }


    @GetMapping
    public  ResponseEntity<List<ProductResponseDto>> getAllProducts(){
        List<ProductResponseDto> products = productService.findAll();
        return new ResponseEntity<>(products , HttpStatus.OK);
    }
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<Product>> getAllProductsByCategory(@PathVariable Long categoryId) {
        List<Product> products = new ArrayList<Product>();
        if (categoryId != null) {
            products = productService.getProductsByCategory(categoryId);
        }
//        else {
//            products = productService.findAll();
//        }
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable long id) {
        Optional<Product> productOptional = productService.getProductById(id);
        if(productOptional.isPresent()){
            Product product = productOptional.get();
            return new ResponseEntity<>(product , HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product createdProduct = productService.createProduct(product);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        Product updatedProduct = productService.updateProduct(id, product);
        return updatedProduct != null
                ? ResponseEntity.ok(updatedProduct)
                : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProductsByName(@RequestParam("name") String name) {
        List<Product> products = productService.searchProductsByName(name);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    //AI
    @GetMapping("/{productId}/recommendations")
    public ResponseEntity<List<ProductResponseDto>> getRecommendations(
            @PathVariable Long productId,
            @RequestParam(defaultValue = "5") int limit) {

        return ResponseEntity.ok(
                recommendationService.getSimilarProducts(productId, limit)
        );
    }

}
