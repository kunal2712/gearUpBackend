package com.gearup.gearupbackend.controller;

import com.gearup.gearupbackend.model.Category;
import com.gearup.gearupbackend.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/gearup/category")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService){
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity <List<Category>> getAllCategories(){
        List<Category> categories = categoryService.getAllCategories();
        return new ResponseEntity<>(categories , HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Category> createCategory(@RequestBody Category category){
        Category createdCategory = categoryService.createCategory(category);
        return new ResponseEntity<>(createdCategory , HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@RequestParam long id){
        Optional<Category> categoryOptional = categoryService.getCategoryById(id);
        if(categoryOptional.isPresent()){
            Category category = categoryOptional.get();
            return new ResponseEntity<>(category , HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public  ResponseEntity<Category> updateCategory(@RequestParam long id , @RequestBody Category category){
        Category updatedCategory = categoryService.updateCategory(id, category);
        if(updatedCategory == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }else{
            return new ResponseEntity<>(updatedCategory , HttpStatus.OK);
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }



}
