package com.gearup.gearupbackend.dto;

import com.gearup.gearupbackend.model.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponseDto{
    private Long id;
    private String name;


}
