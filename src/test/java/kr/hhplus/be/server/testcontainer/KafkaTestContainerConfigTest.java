package kr.hhplus.be.server.testcontainer;

import kr.hhplus.be.server.testsupport.AbstractIntegrationTest;
import kr.hhplus.be.server.testsupport.TestKafkaConsumer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.concurrent.TimeUnit;


public class KafkaTestContainerConfigTest extends AbstractIntegrationTest {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private TestKafkaConsumer testConsumer;

    @Test
    @DisplayName("Kafka 프로듀서/컨슈머 동작 확인")
    void kafkaContainer_shouldProduceAndConsume() throws Exception {
        // given
        String topic = "test-topic";
        String key = "my-key";
        String value = "hello-kafka";

        // when
        kafkaTemplate.send(topic, key, value).get();

        // then
        String consumed = testConsumer.getReceivedMessage(10, TimeUnit.SECONDS);
        Assertions.assertThat(consumed).isEqualTo(value);
    }
}
