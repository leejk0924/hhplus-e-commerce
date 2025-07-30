package kr.hhplus.be.server.product.infrastructure.adapter;

import kr.hhplus.be.server.product.application.repository.ProductRepository;
import kr.hhplus.be.server.product.domain.entity.Product;
import kr.hhplus.be.server.product.infrastructure.persistence.jpa.ProductEntityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ProductRepositoryImplTest {
    private ProductRepository sut;
    @Mock
    private ProductEntityRepository productEntityRepository;
    @BeforeEach
    void setUp() {
        sut = new ProductRepositoryImpl(productEntityRepository);
    }
    @DisplayName("[RepositoryImpl:단위테스트] : 상품 단건 조회 테스트")
    @Test
    void 상품_조회_성공_테스트() throws Exception {
        // Given
        long productId = 1L;
        int price = 1000;
        int stockQuantity = 30;
        Product initProduct = Product.of(productId, "test", price, stockQuantity);
        given(productEntityRepository.findById(productId)).willReturn(Optional.of(initProduct));

        // When
        Product target = sut.loadProduct(productId);

        // Then
        verify(productEntityRepository).findById(anyLong());
        assertThat(target).isEqualTo(initProduct);
    }
    @DisplayName("[RepositoryImpl:단위테스트] : 상품 전체 조회 테스트")
    @Test
    void 상품_전체_조회_성공_테스트() throws Exception {
        // Given
        Product sample1 = Product.of(1L, "test1", 1000, 30);
        Product sample2 = Product.of(2L, "test2", 2000, 100);
        Product sample3 = Product.of(3L, "test3", 3000, 500);
        List<Product> initData = List.of(sample1, sample2, sample3);
        given(productEntityRepository.findAll()).willReturn(initData);

        // When
        List<Product> result = sut.loadAllProducts();
        // Then
        assertThat(result).isEqualTo(initData);
    }

}