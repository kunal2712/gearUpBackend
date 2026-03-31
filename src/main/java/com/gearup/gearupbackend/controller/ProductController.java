package com.gearup.gearupbackend.controller;


import com.gearup.gearupbackend.model.Category;
import com.gearup.gearupbackend.model.Product;
import com.gearup.gearupbackend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/gearup/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }


    @GetMapping
    public  ResponseEntity<List<Product>> getAllProducts(){
        List<Product> products = productService.findAll();
        return new ResponseEntity<>(products , HttpStatus.OK);
    }
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<Product>> getAllProductsByCategory(@PathVariable Long categoryId) {
        List<Product> products;
        if (categoryId != null) {
            products = productService.getProductsByCategory(categoryId);
        } else {
            products = productService.findAll();
        }
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
}
