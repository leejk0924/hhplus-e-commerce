package kr.hhplus.be.infrastructure.kafka;

import kr.hhplus.be.infrastructure.kafka.config.KafkaConfig;
import kr.hhplus.be.infrastructure.kafka.repository.KafkaRepository;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Import;

@AutoConfiguration
@Import({
    KafkaConfig.class,
    KafkaRepository.class
})
public class KafkaAutoConfiguration {
}