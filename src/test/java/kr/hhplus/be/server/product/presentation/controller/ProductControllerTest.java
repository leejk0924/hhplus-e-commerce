package kr.hhplus.be.server.product.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.hhplus.be.server.product.application.dto.ProductDto;
import kr.hhplus.be.server.product.application.service.ProductService;
import kr.hhplus.be.server.product.domain.entity.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
@AutoConfigureMockMvc
class ProductControllerTest {
    @MockitoBean
    private ProductService productService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("[Controller:단위테스트] : 상품 단건 조회 테스트")
    @Test
    void 상품_단건_조회_성공_테스트() throws Exception {
        // Given
        long productId = 1L;
        String productName = "test";
        int initPrice = 1000;
        int stockQuantity = 30;
        Product initProduct = Product.of(productId, productName, initPrice, stockQuantity);
        given(productService.loadProduct(anyLong())).willReturn(ProductDto.toDto(initProduct));

        // When && Then
        mockMvc.perform(
                        get("/api/products/{id}", productId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productName").value(productName))
                .andExpect(jsonPath("$.productPrice").value(initPrice))
                .andExpect(jsonPath("$.stockQuantity").value(stockQuantity));
    }
    @DisplayName("[Controller:단위테스트] : 상품 전체 조회 테스트")
    @Test
    void 상품_전체_조회_성공_테스트() throws Exception {
        // Given
        var p1 = Product.of(1L, "test1", 1000, 30);
        var p2 = Product.of(2L, "test2", 2000, 100);
        var p3 = Product.of(3L, "test3", 3000, 500);
        List<Product> products = List.of(p1, p2, p3);
        List<ProductDto> productDtos = products.stream().map(ProductDto::toDto).toList();
        given(productService.loadAllProduct()).willReturn(productDtos);

        // When && Then
        mockMvc.perform(
                        get("/api/products")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productName").value("test1"))
                .andExpect(jsonPath("$[0].productPrice").value(1000))
                .andExpect(jsonPath("$[0].stockQuantity").value(30))
                .andExpect(jsonPath("$[1].productName").value("test2"))
                .andExpect(jsonPath("$[2].productName").value("test3"));
    }
    @Test
    void 인기_상품_조회_성공_테스트() throws Exception {
        // Given
        var p1 = Product.of(1L, "best1", 5000, 20);
        var p2 = Product.of(2L, "best2", 6000, 15);
        var popular = List.of(
                ProductDto.toDto(p1),
                ProductDto.toDto(p2)
        );
        given(productService.loadPopularProduct()).willReturn(popular);

        // When & Then
        mockMvc.perform(
                        get("/api/products/popular")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productName").value("best1"))
                .andExpect(jsonPath("$[1].productName").value("best2"));
    }
}