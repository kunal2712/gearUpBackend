package com.gearup.gearupbackend.service;

import com.gearup.gearupbackend.dto.ProductResponseDto;
import com.gearup.gearupbackend.mapper.CategoryMapper;
import com.gearup.gearupbackend.model.Product;
import com.gearup.gearupbackend.repository.ProductRepository;
import com.gearup.gearupbackend.repository.SimilarProductProjection;
import com.gearup.gearupbackend.service.RecommendationService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecommendationServiceImpl implements RecommendationService {

    private final ProductRepository productRepository;
    private final CategoryMapper categoryMapper;

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponseDto> getSimilarProducts(
            Long productId,
            int limit
    ) {
        int safeLimit = Math.max(1, Math.min(limit, 20));

        List<SimilarProductProjection> similarProducts =
                productRepository.findSimilarProducts(productId, safeLimit);

        if (similarProducts.isEmpty()) {
            return List.of();
        }

        List<Long> productIds = similarProducts.stream()
                .map(SimilarProductProjection::getProductId)
                .toList();

        Map<Long, Product> productsById = productRepository
                .findAllById(productIds)
                .stream()
                .collect(Collectors.toMap(
                        Product::getId,
                        Function.identity()
                ));

        return productIds.stream()
                .map(productsById::get)
                .filter(product -> product != null)
                .map(product -> ProductResponseDto.builder()
                        .id(product.getId())
                        .name(product.getName())
                        .description(product.getDescription())
                        .price(product.getPrice())
                        .imageUrl(product.getImageUrl())
                        .stockQuantity(product.getStockQuantity())
                        .createdAt(product.getCreatedAt())
                        .updatedAt(product.getUpdatedAt())
                        .category(categoryMapper.toCategoryResponseDto(
                                product.getCategory()
                        ))
                        .build())
                .toList();
    }
}
