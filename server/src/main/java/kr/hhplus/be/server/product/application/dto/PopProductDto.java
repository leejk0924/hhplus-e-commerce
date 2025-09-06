package kr.hhplus.be.server.product.application.dto;

import kr.hhplus.be.server.product.domain.entity.Product;

import java.time.LocalDateTime;

public record PopProductDto(
        Integer salesCount,
        String productName,
        int productPrice,
        LocalDateTime createdAt,
        LocalDateTime updatedAt

) {
    public static PopProductDto toDto(Product product, Integer salesCount) {
        return new PopProductDto(
                salesCount,
                product.getProductName(),
                product.getPrice(),
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }
}
