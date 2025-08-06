package kr.hhplus.be.server.product.application.repository;

import kr.hhplus.be.server.product.domain.entity.Product;

import java.util.List;

public interface ProductRepository {
    Product loadProduct(long l);
    List<Product> loadAllProducts();

    List<Product> loadPopularProducts();
}
