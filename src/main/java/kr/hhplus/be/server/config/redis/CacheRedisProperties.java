package kr.hhplus.be.server.config.redis;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "spring.cache.redis")
public class CacheRedisProperties {
    private String master;
    private int database;
    private List<Sentinel> sentinels;

    @Data
    public static class Sentinel {
        private String host;
        private int port;
    }
}
