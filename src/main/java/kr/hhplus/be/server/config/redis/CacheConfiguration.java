package kr.hhplus.be.server.config.redis;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import kr.hhplus.be.server.common.mapper.CommonObjectMapper;
import kr.hhplus.be.server.common.redis.cache.CacheNames;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.stream.Collectors;

@Configuration
@EnableCaching
@EnableConfigurationProperties(CacheRedisProperties.class)
@RequiredArgsConstructor
public class CacheConfiguration {
    private static final Long DEFAULT_EXPIRATION_MIN = 60L;
    private final CacheRedisProperties cacheRedisProperties;
    @Bean
    public RedisConnectionFactory cacheRedisConnectionFactory() {
        RedisSentinelConfiguration sentinelConfig = new RedisSentinelConfiguration();
        sentinelConfig.master(cacheRedisProperties.getMaster());
        cacheRedisProperties.getSentinels().forEach(s-> sentinelConfig.sentinel(s.getHost(), s.getPort()));
        sentinelConfig.setDatabase(cacheRedisProperties.getDatabase());
        return new LettuceConnectionFactory(sentinelConfig);
    }

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        var objectMapper = new CommonObjectMapper();
        objectMapper.activateDefaultTyping(
                LaissezFaireSubTypeValidator.instance,
                ObjectMapper.DefaultTyping.EVERYTHING,
                JsonTypeInfo.As.PROPERTY
        );

        var cacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(DEFAULT_EXPIRATION_MIN))
                .disableCachingNullValues()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer(objectMapper)));

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(cacheConfiguration)
                .withInitialCacheConfigurations(
                        CacheNames.getAll()
                                .stream()
                                .collect(Collectors.toMap(
                                        CacheNames.CacheName::name,
                                        cacheName -> cacheConfiguration.entryTtl(Duration.of(cacheName.expirationTime(), cacheName.timeUnit().toChronoUnit())))
                                )
                )
                .build();
    }
}
