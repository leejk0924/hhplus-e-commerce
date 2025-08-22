package kr.hhplus.be.server.order.application.repository;

import java.util.Map;

public interface SalesProductRepository {
    void addSalesQuantity(Map<String, Long> salesProducts);
}
