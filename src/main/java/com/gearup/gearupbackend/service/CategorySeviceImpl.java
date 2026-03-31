package com.gearup.gearupbackend.service;

import com.gearup.gearupbackend.model.Category;
import com.gearup.gearupbackend.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Service
public class CategorySeviceImpl implements CategoryService {


    private final CategoryRepository categoryRepository;

    @Autowired
    public CategorySeviceImpl(CategoryRepository categoryRepository){
        this.categoryRepository = categoryRepository;
    }


    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Optional<Category> getCategoryById(long id) {
        return categoryRepository.findById(id);
    }

    @Override
    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public Category updateCategory(long id, Category category) {
        Optional<Category> existingCategory = categoryRepository.findById(id);
        if(existingCategory.isPresent()){
            Category updatedCategory = existingCategory.get();
            updatedCategory.setName(category.getName());
            return categoryRepository.save(updatedCategory);
        }
        return null;
    }

    @Override
    public void deleteCategory(long id) {
        Optional<Category> deleteCategory = categoryRepository.findById(id);
        if(deleteCategory.isPresent()) {
            categoryRepository.deleteById(id);
        }
    }
}
