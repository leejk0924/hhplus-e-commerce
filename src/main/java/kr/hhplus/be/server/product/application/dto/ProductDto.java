package kr.hhplus.be.server.product.application.dto;

import kr.hhplus.be.server.product.domain.entity.Product;

import java.time.LocalDateTime;

public record ProductDto(
        String productName,
        int productPrice,
        int stockQuantity,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static ProductDto toDto(Product product) {
        return new ProductDto(
                product.getProductName(),
                product.getPrice(),
                product.getStockQuantity(),
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }
}
