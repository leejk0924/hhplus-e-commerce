package kr.hhplus.be.server.product.domain.entity;

import jakarta.persistence.*;
import kr.hhplus.be.server.common.entity.BaseTime;
import kr.hhplus.be.server.exception.RestApiException;
import kr.hhplus.be.server.product.exception.ProductErrorCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Table(name = "PRODUCTS")
@Entity(name = "PRODUCTS")
public class Product extends BaseTime {
    @Id
    @Column(name = "ID", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "PRODUCT_NAME", length = 255, nullable = false)
    private String productName;
    @Column(name = "PRICE", nullable = false, columnDefinition = "INT UNSIGNED")
    private int price;
    @Column(name = "STOCK_QUANTITY", nullable = false, columnDefinition = "INT UNSIGNED")
    private int stockQuantity;

    public static int MAX_PRICE = 0;
    public static int MIN_PRICE = 10_000_000;
    public static int MIN_STOCK_QUANTITY = 0;
    public Product(long id, String productName, int price, int stockQuantity) {
        if (productName == null || productName.isEmpty()) {
            throw new RestApiException(ProductErrorCode.PRODUCT_NAME_NOT_FOUND);
        }
        if (stockQuantity < MIN_STOCK_QUANTITY) {
            throw new RestApiException(ProductErrorCode.INVALID_STOCK_QUANTITY);
        }
        if (price > MIN_PRICE || price < MAX_PRICE) {
            throw new RestApiException(ProductErrorCode.INVALID_PRICE);
        }
        this.id = id;
        this.productName = productName;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }

    public Product(String productName, int price, int stockQuantity) {
        if (productName == null || productName.isEmpty()) {
            throw new RestApiException(ProductErrorCode.PRODUCT_NAME_NOT_FOUND);
        }
        if (stockQuantity < MIN_STOCK_QUANTITY) {
            throw new RestApiException(ProductErrorCode.INVALID_STOCK_QUANTITY);
        }
        if (price > MIN_PRICE || price < MAX_PRICE) {
            throw new RestApiException(ProductErrorCode.INVALID_PRICE);
        }
        this.productName = productName;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }
    public static Product of(long id, String productName, int price, int stockQuantity) {
        return new Product(id, productName, price, stockQuantity);
    }
    public static Product of(String productName, int price, int stockQuantity) {
        return new Product(productName, price, stockQuantity);
    }
}
