package kr.hhplus.be.server.product.application.repository;


import java.util.Map;

public interface ProductRankingRepository {
    void initSortedSet();
    void unionLast3Days();
    Map<Long, Integer> getTopNProducts();
}
