package kr.hhplus.be.server.order.infrastructure.adapter;

import kr.hhplus.be.server.order.application.repository.SalesProductRepository;
import kr.hhplus.be.server.order.infrastructure.persistence.redis.RedisSalesProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class SalesProductRepositoryImpl implements SalesProductRepository {
    private final RedisSalesProductRepository redisSalesProductRepository;
    @Value("${db.product.popular}")
    private String baseKey;
    @Override
    public void addSalesQuantity(Map<String, Long> salesProducts) {
        String today = LocalDate.now().format(DateTimeFormatter.ISO_DATE);
        String key = baseKey + ":" + today;
        redisSalesProductRepository.updateSalesQuantity(key, salesProducts, Duration.of(4, ChronoUnit.DAYS));
    }
}
