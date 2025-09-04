package kr.hhplus.be.server.order.infrastructure.persistence.kafka;

import kr.hhplus.be.infrastructure.kafka.repository.KafkaRepository;
import kr.hhplus.be.server.testsupport.AbstractIntegrationTest;
import kr.hhplus.be.server.testsupport.TestKafkaConsumer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class KafkaRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    private KafkaRepository sut;

    @Autowired
    private TestKafkaConsumer testKafkaConsumer;

    @ParameterizedTest
    @ValueSource(strings = {"first-message", "second-message", "third-message"})
    @DisplayName("메시지 전송 성공 시 확인")
    void sendMessage_Success(String message) throws Exception {
        // Given
        String topic = "test-topic";

        // When
        sut.sendMessage(topic, message);

        // Then
        String consumed = testKafkaConsumer.getReceivedMessage(10, TimeUnit.SECONDS);
        assertThat(consumed).isEqualTo(message);
    }
}