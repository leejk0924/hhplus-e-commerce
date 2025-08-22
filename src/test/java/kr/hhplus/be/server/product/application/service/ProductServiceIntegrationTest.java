package kr.hhplus.be.server.product.application.service;

import jakarta.transaction.Transactional;
import kr.hhplus.be.server.product.application.dto.PopProductDto;
import kr.hhplus.be.server.product.application.dto.ProductDto;
import kr.hhplus.be.server.product.domain.entity.Product;
import kr.hhplus.be.server.product.infrastructure.persistence.jpa.ProductEntityRepository;
import kr.hhplus.be.server.testsupport.AbstractIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class ProductServiceIntegrationTest extends AbstractIntegrationTest {
    @Autowired
    private ProductEntityRepository productEntityRepository;
    @Autowired
    private ProductService sut;
    private HashMap<Long, Product> dummyDB;
    @Autowired
    private StringRedisTemplate redisTemplate;

    @BeforeEach
    void setUp() {
        dummyDB = new HashMap<>();
    }

    @DisplayName("[Service:통합테스트] : 상품 단건 조회 테스트")
    @Test
    void 상품_단건_조회_통합_성공_테스트() throws Exception {
        // Given
        String productName = "test";
        int price = 1000;
        int stockQuantity = 100;
        Product saved = productEntityRepository.save(Product.of(productName, price, stockQuantity));

        // When
        ProductDto result = sut.loadProduct(saved.getId());

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
        Product p2 = Product.of("상품B", 1500, 100);
        Product p3 = Product.of("상품C", 2000, 100);
        List<Product> products = List.of(p1, p2, p3);
        productEntityRepository.saveAll(products);
        List<PopProductDto> expected = List.of(
                PopProductDto.toDto(p3, 30),
                PopProductDto.toDto(p2, 20),
                PopProductDto.toDto(p1, 10)
        );
        redisTemplate.opsForZSet().add("DB:RANKING_PRODUCT:"+ LocalDate.now(), String.valueOf(p3.getId()), 30);
        redisTemplate.opsForZSet().add("DB:RANKING_PRODUCT:"+ LocalDate.now(), String.valueOf(p2.getId()), 20);
        redisTemplate.opsForZSet().add("DB:RANKING_PRODUCT:"+ LocalDate.now(), String.valueOf(p1.getId()), 10);

        // When
        List<PopProductDto> popProductDtos = sut.loadPopularProduct();
        // Then
        assertThat(popProductDtos).isEqualTo(expected);
    }
}