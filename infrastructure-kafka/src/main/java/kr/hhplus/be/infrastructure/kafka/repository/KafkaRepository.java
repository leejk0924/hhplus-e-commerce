package kr.hhplus.be.infrastructure.kafka.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Repository;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Repository
@RequiredArgsConstructor
public class KafkaRepository {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendMessage(String topic, Object message) {
        try {
            kafkaTemplate.send(topic, message);
            log.debug("Message sent to topic: {} with payload: {}", topic, message);
        } catch (Exception e) {
            log.error("Failed to send message to topic: {}", topic, e);
            throw new RuntimeException("Message sending failed", e);
        }
    }

    public void sendMessage(String topic, String key, Object message) {
        try {
            kafkaTemplate.send(topic, key, message);
            log.info("Message sent to topic: {} with key: {} and payload: {}", topic, key, message);
        } catch (Exception e) {
            log.error("Failed to send message to topic: {} with key: {}", topic, key, e);
            throw new RuntimeException("Message sending failed", e);
        }
    }

    public void sendMessageSync(String topic, String key, Object message) {
        try {
            CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topic, key, message);
            SendResult<String, Object> result = future.get();
            log.info("Message sent successfully. Topic: {}, Partition: {}, Offset: {}",
                    topic, result.getRecordMetadata().partition(), result.getRecordMetadata().offset());
        } catch (Exception e) {
            log.error("Failed to send message synchronously to topic: {}", topic, e);
            throw new RuntimeException("Synchronous message sending failed", e);
        }
    }
}