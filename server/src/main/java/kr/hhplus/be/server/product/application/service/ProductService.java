package kr.hhplus.be.server.product.application.service;

import kr.hhplus.be.server.common.redis.cache.CacheNames;
import kr.hhplus.be.server.product.application.dto.PopProductDto;
import kr.hhplus.be.server.product.application.dto.ProductDto;
import kr.hhplus.be.server.product.application.repository.ProductRankingRepository;
import kr.hhplus.be.server.product.application.repository.ProductRepository;
import kr.hhplus.be.server.product.domain.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductRankingRepository productRankingRepository;
    @Cacheable(value = CacheNames.PRODUCT_DETAIL, key = "'PRODUCT_ID' + #productId")
    @Transactional(readOnly = true)
    public ProductDto loadProduct(Long productId) {
        Product product = productRepository.loadProduct(productId);
        return ProductDto.toDto(product);
    }
    public List<ProductDto> loadAllProduct() {
        List<Product> products = productRepository.loadAllProducts();
        return products.stream().map(ProductDto::toDto).toList();
    }

    @Cacheable(value = CacheNames.POP_PRODUCTS, key = "'POP_PRODUCT_LIST:'+ T(java.time.LocalDate).now().toString")
    @Transactional(readOnly = true)
    public List<PopProductDto> loadPopularProduct() {
        Map<Long, Integer> topNProducts = productRankingRepository.getTopNProducts();
        List<Long> productsId = new ArrayList<>(topNProducts.keySet());

        List<Product> products = productRepository.loadTopNProducts(productsId);
        Map<Long, Product> productById = products.stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));

        return productsId.stream()
                .map(id -> {
                    Product p = productById.get(id);
                    if (p == null) return null;
                    Integer sales = topNProducts.get(id);
                    return PopProductDto.toDto(p, sales);
                })
                .sorted(Comparator.comparingInt(PopProductDto::salesCount).reversed())
                .toList();
    }

    @CachePut(value = CacheNames.POP_PRODUCTS, key = "'POP_PRODUCT_LIST:' + T(java.time.LocalDate).now().toString")
    @Transactional(readOnly = true)
    public List<PopProductDto> refreshPopularProduct() {
        productRankingRepository.initSortedSet();
        productRankingRepository.unionLast3Days();
        Map<Long, Integer> topNProducts = productRankingRepository.getTopNProducts();
        List<Long> productsId = new ArrayList<>(topNProducts.keySet());

        List<Product> products = productRepository.loadTopNProducts(productsId);
        Map<Long, Product> productById = products.stream()
                .collect(java.util.stream.Collectors.toMap(Product::getId, Function.identity()));

        return productsId.stream()
                .map(id -> {
                    Product p = productById.get(id);
                    if (p == null) return null;
                    Integer sales = topNProducts.get(id);
                    return PopProductDto.toDto(p, sales);
                })
                .filter(java.util.Objects::nonNull)
                .toList();
    }
}
