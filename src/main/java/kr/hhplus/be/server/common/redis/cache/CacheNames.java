package kr.hhplus.be.server.common.redis.cache;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class CacheNames  {
    public static final String POP_PRODUCTS = "PRODUCT::POP";
    private static final long POP_PRODUCTS_EXPIRATION = 1L;
    public static final String PRODUCT_DETAIL = "PRODUCT::DETAIL";
    private static final long PRODUCT_DETAILS_EXPIRATION = 60L;

    public static List<CacheName> getAll() {
        return List.of(
                new CacheName(POP_PRODUCTS, POP_PRODUCTS_EXPIRATION, TimeUnit.DAYS),
                new CacheName(PRODUCT_DETAIL, PRODUCT_DETAILS_EXPIRATION, TimeUnit.HOURS)
        );
    }

    public record CacheName(
            String name,
            long expirationTime,
            TimeUnit timeUnit
    ) {
    }
}
