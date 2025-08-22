package kr.hhplus.be.server.coupon.application.repository;

import java.util.List;

public interface CouponIssuedRepository {
    void markCouponIssued(String key, Long userId);
    boolean hasCouponIssued(String key, Long userId);
    int enrollAndGetRank(String key, Long userId);
    List<String> scanKeys(String pattern);
    List<Long> popQueue(String queueKey);
}