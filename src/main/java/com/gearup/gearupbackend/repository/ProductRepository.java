package com.gearup.gearupbackend.repository;

import com.gearup.gearupbackend.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product , Long> {

    List<Product> findAll();
    List<Product> findByCategoryId(Long categoryId);

    List<Product> findByNameContainingIgnoreCase(String name);


    @Query(value = """
    SELECT
        product_id AS "productId",
        similarity
    FROM public.get_similar_products(:productId, :resultLimit)
    """, nativeQuery = true)
    List<SimilarProductProjection> findSimilarProducts(
            @Param("productId") Long productId,
            @Param("resultLimit") Integer resultLimit
    );

}
