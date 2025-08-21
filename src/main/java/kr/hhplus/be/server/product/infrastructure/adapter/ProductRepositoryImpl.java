package kr.hhplus.be.server.product.infrastructure.adapter;

import kr.hhplus.be.server.exception.RestApiException;
import kr.hhplus.be.server.product.application.repository.ProductRepository;
import kr.hhplus.be.server.product.domain.entity.Product;
import kr.hhplus.be.server.product.exception.ProductErrorCode;
import kr.hhplus.be.server.product.infrastructure.persistence.jpa.ProductEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {
    private final ProductEntityRepository productEntityRepository;
    @Override
    public Product loadProduct(long l) {
        return productEntityRepository.findById(l).orElseThrow(()-> new RestApiException(ProductErrorCode.PRODUCT_NOT_FOUND));
    }

    @Override
    public List<Product> loadAllProducts() {
        return productEntityRepository.findAll();
    }
    @Override
    public List<Product> loadPopularProducts() {
        return productEntityRepository.findPopularProducts(LocalDate.now().minusDays(3));
    }

    @Override
    public List<Product> loadTopNProducts(List<Long> productIds) {
        return productEntityRepository.findAllById(productIds);
    }
}
