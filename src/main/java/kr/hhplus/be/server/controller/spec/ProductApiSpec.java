package kr.hhplus.be.server.controller.spec;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.domain.dto.ProductResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "상품", description = "상품 관련 API")
public interface ProductApiSpec {
    @Operation(summary = "상품 조회")
    ResponseEntity<List<ProductResponse.CheckProductDto>> checkProduct();
    @Operation(summary = "인기 상품 조회")
    ResponseEntity<List<ProductResponse.PopularProductsDto>> getPopularProducts();
}
