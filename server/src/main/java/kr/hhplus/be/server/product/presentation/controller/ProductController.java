package kr.hhplus.be.server.product.presentation.controller;

import kr.hhplus.be.server.product.application.dto.PopProductDto;
import kr.hhplus.be.server.product.application.dto.ProductDto;
import kr.hhplus.be.server.product.application.service.ProductService;
import kr.hhplus.be.server.product.presentation.controller.spec.ProductApiSpec;
import kr.hhplus.be.server.product.presentation.dto.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class ProductController implements ProductApiSpec {
    private final ProductService productService;
    @GetMapping("/products/{productId}")
    public ResponseEntity<ProductResponse.CheckProductDto> checkProduct(@PathVariable long productId) {
        ProductDto productDto = productService.loadProduct(productId);
        return ResponseEntity.ok(ProductResponse.CheckProductDto.from(productDto));
    }
    @GetMapping("/products")
    public ResponseEntity<List<ProductResponse.CheckProductDto>> checkAllProduct() {
        List<ProductDto> productDtos = productService.loadAllProduct();
        return ResponseEntity.ok(productDtos.stream().map(ProductResponse.CheckProductDto::from).toList());
    }
    @GetMapping("/products/popular")
    public ResponseEntity<List<ProductResponse.PopularProductsDto>> getPopularProducts() {
        List<PopProductDto> popProductDtos = productService.loadPopularProduct();
        return ResponseEntity.ok(popProductDtos.stream().map(ProductResponse.PopularProductsDto::from).toList());
    }
}
