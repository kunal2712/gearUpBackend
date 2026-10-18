package com.gearup.gearupbackend.service;
import com.gearup.gearupbackend.dto.ProductResponseDto;
import java.util.List;

public interface RecommendationService {

    List<ProductResponseDto> getSimilarProducts(
            Long productId,
            int limit
    );
}
