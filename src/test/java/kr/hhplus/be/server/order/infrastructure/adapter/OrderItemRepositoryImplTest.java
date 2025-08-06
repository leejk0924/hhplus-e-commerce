package kr.hhplus.be.server.order.infrastructure.adapter;

import kr.hhplus.be.server.order.application.repository.OrderItemRepository;
import kr.hhplus.be.server.order.infrastructure.persistence.jpa.OrderItemEntityRepository;
import kr.hhplus.be.server.product.domain.entity.Product;
import kr.hhplus.be.server.stub.StubProduct;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class OrderItemRepositoryImplTest {
    private OrderItemRepository sut;
    @Mock
    private OrderItemEntityRepository orderItemEntityRepository;
    @BeforeEach
    void setUp() {
        // 직접 구현체를 생성해서 인터페이스 변수에 할당
        sut = new OrderItemRepositoryImpl(orderItemEntityRepository);
    }
    @Test
    void 상품_전체_조회_테스트() throws Exception {
        // Given
        List<Long> productsIds = List.of(1L, 2L, 3L);
        StubProduct product1 = new StubProduct(1L, "test1", 2000, 10);
        StubProduct product2 = new StubProduct(2L, "test2", 3000, 20);
        StubProduct product3 = new StubProduct(3L, "test3", 4000, 30);
        List<Product> products = List.of(product1, product2, product3);

        given(orderItemEntityRepository.findProductsByProductIds(anyList())).willReturn(products);

        // When
        List<Product> target = sut.findProductsByIds(productsIds);

        // Then
        assertThat(target.size()).isEqualTo(products.size());
        assertThat(target).usingRecursiveComparison().isEqualTo(products);
    }
}