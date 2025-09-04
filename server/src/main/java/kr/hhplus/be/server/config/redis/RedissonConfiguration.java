package kr.hhplus.be.server.config.redis;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.io.InputStream;
import java.util.Objects;

@Configuration
public class RedissonConfiguration {
    @Bean(destroyMethod = "shutdown")
    public RedissonClient redissonClient(Environment env) throws Exception {
        String inline = env.getProperty("spring.redis.redisson.config");

        if (inline != null && !inline.isBlank()) {
            return Redisson.create(Config.fromYAML(inline));
        }

        try (InputStream is = Objects.requireNonNull(
                getClass().getClassLoader().getResourceAsStream("redisson.yml"),
                "redisson.yml not found"
        )) {
            return Redisson.create(Config.fromYAML(is));
        }
    }
}
