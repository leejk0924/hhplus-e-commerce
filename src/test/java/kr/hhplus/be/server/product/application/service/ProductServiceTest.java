package kr.hhplus.be.server.product.application.service;

import kr.hhplus.be.server.product.application.dto.PopProductDto;
import kr.hhplus.be.server.product.application.dto.ProductDto;
import kr.hhplus.be.server.product.application.repository.ProductRankingRepository;
import kr.hhplus.be.server.product.application.repository.ProductRepository;
import kr.hhplus.be.server.product.domain.entity.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @InjectMocks
    private ProductService sut;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private ProductRankingRepository productRankingRepository;

    @DisplayName("[Service:단위테스트] : 상품 단건 조회 테스트")
    @Test
    void 상품_조회_서비스_테스트() throws Exception {
        // Given
        Long productId = 1L;
        String productName = "test";
        Product initProduct = Product.of(productId, productName, 1000, 30);
        given(productRepository.loadProduct(anyLong())).willReturn(initProduct);
        ProductDto expected = ProductDto.toDto(initProduct);

        // When
        ProductDto result = sut.loadProduct(productId);

        // Then
        verify(productRepository).loadProduct(anyLong());
        assertThat(result).isEqualTo(expected);
    }
    @DisplayName("[Service:단위테스트] : 상품 전체 조회 테스트")
    @Test
    void 상품_전체_조회_서비스_테스트() throws Exception {
        // Given
        Product sample1 = Product.of(1L, "test1", 1000, 30);
        Product sample2 = Product.of(2L, "test2", 2000, 100);
        Product sample3 = Product.of(3L, "test3", 3000, 500);
        List<Product> initData = List.of(sample1, sample2, sample3);
        List<ProductDto> expected = initData.stream().map(ProductDto::toDto).toList();
        given(productRepository.loadAllProducts()).willReturn(initData);

        // When
        List<ProductDto> result = sut.loadAllProduct();

        // Then
        assertThat(result).isEqualTo(expected);
    }
    @DisplayName("[Service:단위테스트] : 지난 3일간 인기 상품 조회 테스트")
    @Test
    void 인기_상품_조회_서비스_테스트() throws Exception {
        // Given
        Product p1 = Product.of(1L, "상품A", 1000, 100);
        Product p2 = Product.of(2L, "상품B", 1500, 100);
        Product p3 = Product.of(3L, "상품C", 2000, 100);
        List<Product> products = List.of(p1, p2, p3);

        List<PopProductDto> expected = List.of(
                PopProductDto.toDto(p3, 30),
                PopProductDto.toDto(p2, 20),
                PopProductDto.toDto(p1, 10)
        );
        Map<Long, Integer> salesMap = Map.of(
                3L, 30,
                2L, 20,
                1L, 10
        );
        given(productRankingRepository.getTopNProducts()).willReturn(salesMap);
        given(productRepository.loadTopNProducts(anyList())).willReturn(products);
        // WHEN
        List<PopProductDto> popProductDtos = sut.loadPopularProduct();

        // THEN
        assertThat(popProductDtos).isEqualTo(expected);
    }
}