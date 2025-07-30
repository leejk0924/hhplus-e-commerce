package kr.hhplus.be.server.product.application.service;

import kr.hhplus.be.server.product.application.dto.ProductDto;
import kr.hhplus.be.server.product.application.repository.ProductRepository;
import kr.hhplus.be.server.product.domain.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    public ProductDto loadProduct(Long productId) {
        Product product = productRepository.loadProduct(productId);
        return ProductDto.toDto(product);
    }
    public List<ProductDto> loadAllProduct() {
        List<Product> products = productRepository.loadAllProducts();
        return products.stream().map(ProductDto::toDto).toList();
    }
    public List<ProductDto> loadPopularProduct() {
        List<Product> products = productRepository.loadPopularProducts();
        return products.stream().map(ProductDto::toDto).toList();
    }
}
