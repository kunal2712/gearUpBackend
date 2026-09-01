package com.gearup.gearupbackend.mapper;

import com.gearup.gearupbackend.dto.CategoryResponseDto;
import com.gearup.gearupbackend.model.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public CategoryResponseDto toCategoryResponseDto(Category category){
        return CategoryResponseDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }
}
