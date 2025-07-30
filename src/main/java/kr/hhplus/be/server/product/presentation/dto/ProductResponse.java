package kr.hhplus.be.server.product.presentation.dto;

import kr.hhplus.be.server.product.application.dto.ProductDto;

import java.time.LocalDateTime;

public class ProductResponse {
    public record CheckProductDto(
            String productName,
            int productPrice,
            int stockQuantity,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        public CheckProductDto(ProductDto productDto) {
            this(
                    productDto.productName(),
                    productDto.productPrice(),
                    productDto.stockQuantity(),
                    productDto.createdAt(),
                    productDto.updatedAt()
            );
        }
        public static CheckProductDto from(ProductDto productDto) {
            return new CheckProductDto(productDto);
        }
    }
    public record PopularProductsDto(
            String productName,
            int productPrice,
            int stockQuantity,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        public PopularProductsDto(ProductDto productDto) {
            this(
                    productDto.productName(),
                    productDto.productPrice(),
                    productDto.stockQuantity(),
                    productDto.createdAt(),
                    productDto.updatedAt()
            );
        }
        public static PopularProductsDto from(ProductDto productDto) {
            return new PopularProductsDto(productDto);
        }
    }
}