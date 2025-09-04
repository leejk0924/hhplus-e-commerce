package kr.hhplus.be.server.testsupport;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.TestConfiguration;
import org.testcontainers.kafka.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration
public class KafkaTestContainerConfig {
    private static final DockerImageName KAFKA_IMAGE = DockerImageName.parse("apache/kafka-native:3.8.0");
    private static final KafkaContainer KAFKA_CONTAINER;

    private static final Logger log = LoggerFactory.getLogger(KafkaTestContainerConfig.class);

    static {
        KAFKA_CONTAINER = new KafkaContainer(KAFKA_IMAGE);
        KAFKA_CONTAINER.start();
    }

    public static KafkaContainer getContainer() {
        return KAFKA_CONTAINER;
    }
}
