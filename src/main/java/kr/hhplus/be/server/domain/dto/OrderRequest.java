package kr.hhplus.be.server.domain.dto;

import lombok.Getter;

import java.util.List;

public class OrderRequest {
    @Getter
    public static class OrderDto{
        private Long userId;
        private List<ProductsDto> products;
    }
    @Getter
    public static class ProductsDto {
        private Long productId;
        private int quantity;
    }
    @Getter
    public static class ProcessProductsDto {
        private Long userId;
        private Long orderId;
        private int userCouponId;
    }
}
