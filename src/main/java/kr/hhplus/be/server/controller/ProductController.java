package kr.hhplus.be.server.controller;

import kr.hhplus.be.server.controller.spec.ProductApiSpec;
import kr.hhplus.be.server.domain.dto.ProductResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ProductController implements ProductApiSpec {
    @GetMapping("/products")
    public ResponseEntity<List<ProductResponse.CheckProductDto>> checkProduct() {
        ProductResponse.CheckProductDto sample1 = ProductResponse
                .CheckProductDto
                .builder()
                .productName("상품1")
                .price(1000)
                .stockQuantity(100)
                .createAt(LocalDateTime.now())
                .build();
        ProductResponse.CheckProductDto sample2 = ProductResponse
                .CheckProductDto
                .builder()
                .productName("상품2")
                .price(2000)
                .stockQuantity(150)
                .createAt(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(List.of(sample1, sample2));
    }
    @GetMapping("/products/popular")
    public ResponseEntity<List<ProductResponse.PopularProductsDto>> getPopularProducts() {
        ProductResponse.PopularProductsDto sample1 = ProductResponse
                .PopularProductsDto
                .builder()
                .productName("인기 상품1")
                .price(1000)
                .stockQuantity(100)
                .rank(1)
                .createAt(LocalDateTime.now())
                .build();

        ProductResponse.PopularProductsDto sample2 = ProductResponse
                .PopularProductsDto
                .builder()
                .productName("인기 상품2")
                .price(3000)
                .stockQuantity(50)
                .rank(2)
                .createAt(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(List.of(sample1, sample2));
    }
}
