package kr.hhplus.be.server.domain.dto;

import lombok.*;

import java.time.LocalDateTime;

public class ProductResponse {
    @Getter
    @Builder
    @AllArgsConstructor
    public static class CheckProductDto {
        private String productName;
        private int price;
        private int stockQuantity;
        private LocalDateTime createAt;
        private LocalDateTime updateAt;
    }
    @Getter
    @Builder
    @AllArgsConstructor
    public static class PopularProductsDto {
        private String productName;
        private int price;
        private int stockQuantity;
        private int rank;
        private LocalDateTime createAt;
        private LocalDateTime updateAt;
    }
}
