package kr.hhplus.be.server.product.application.service;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import kr.hhplus.be.server.product.application.dto.ProductDto;
import kr.hhplus.be.server.product.domain.entity.Product;
import kr.hhplus.be.server.product.infrastructure.adapter.ProductRepositoryImpl;
import kr.hhplus.be.server.product.infrastructure.persistence.jpa.ProductEntityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ActiveProfiles("test")
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class ProductServiceIntegrationTest {
    @MockitoBean
    private ProductEntityRepository productEntityRepository;
    @Autowired
    private ProductService sut;
    private HashMap<Long, Product> dummyDB;
    @Autowired
    private EntityManager em;

    @BeforeEach
    void setUp() {
        dummyDB = new HashMap<>();
    }

    @DisplayName("[Service:통합테스트] : 상품 단건 조회 테스트")
    @Test
    void 상품_단건_조회_통합_성공_테스트() throws Exception {
        // Given
        dummyDB.put(1L, Product.of(1L, "test", 1000, 100));
        long productId = 1L;
        String productName = "test";
        int price = 1000;
        int stockQuantity = 100;

        given(productEntityRepository.findById(anyLong()))
                .willAnswer(i -> {
                    long id = i.getArgument(0);
                    return Optional.of(dummyDB.get(id));
                });

        // When
        ProductDto result = sut.loadProduct(productId);

        // Then
        assertThat(result.productName()).isEqualTo(productName);
        assertThat(result.productPrice()).isEqualTo(price);
        assertThat(result.stockQuantity()).isEqualTo(stockQuantity);
    }
    @DisplayName("[Service:통합테스트] : 인기상품 조회 테스트")
    @Test
    void 인기_상품_조회_통합_성공_테스트() throws Exception {
        // Given
        Product p1 = Product.of("상품A", 1000, 100);
        Product p2 = Product.of( "상품B", 1500, 100);
        Product p3 = Product.of("상품C", 2000, 100);
        List<Product> products = List.of(p1, p2, p3);
        List<ProductDto> expected = products.stream().map(ProductDto::toDto).toList();
        given(productEntityRepository.findPopularProducts(any(LocalDate.class))).willReturn(products);

        // When
        List<ProductDto> productDtos = sut.loadPopularProduct();

        // Then
        assertThat(productDtos).isEqualTo(expected);
    }
}