    package com.gearup.gearupbackend.model;

    import jakarta.persistence.*;
    import lombok.*;
    import org.hibernate.annotations.CreationTimestamp;
    import org.hibernate.annotations.UpdateTimestamp;

    import java.math.BigDecimal;
    import java.time.LocalDateTime;

    @Entity
    @Table(name = "products")
    @Data
    @NoArgsConstructor
    public class Product {

        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE)
        private Long id;

        @ManyToOne
        @JoinColumn(name = "category_id" , nullable = false)
        private Category category;

        @Column(nullable = false)
        private String name;

        @Column(columnDefinition = "TEXT")
        private String description;

        @Column(nullable = false, precision = 10, scale = 2)
        private BigDecimal price;

        private String imageUrl;

        @Column(nullable = false)
        private Integer stockQuantity;

        @CreationTimestamp
        private LocalDateTime createdAt;


        @UpdateTimestamp
        private LocalDateTime updatedAt;

        public Product(Category category, String name, BigDecimal price, Integer stockQuantity) {
            this.category = category;
            this.name = name;
            this.price = price;
            this.stockQuantity = stockQuantity;
        }

    }
