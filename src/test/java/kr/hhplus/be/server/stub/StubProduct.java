package kr.hhplus.be.server.stub;


import kr.hhplus.be.server.product.domain.entity.Product;

public class StubProduct extends Product {
    public StubProduct(Long id, String productName, int price, int stockQuantity) {
        super(id, productName, price, stockQuantity);
    }
    public static StubProduct of(long id, String productName, int price, int stockQuantity) {
        return new StubProduct(id, productName, price, stockQuantity);
    }

}
