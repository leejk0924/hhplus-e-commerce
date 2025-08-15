package kr.hhplus.be.server.testsupport;

import org.springframework.boot.test.context.TestConfiguration;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration
public class RedisTestContainerConfig {
    private static GenericContainer<?> REDIS_CONTAINER;
    private static final DockerImageName REDIS_IMAGE = DockerImageName.parse("redis:latest");

    static {
        REDIS_CONTAINER = new GenericContainer<>(REDIS_IMAGE)
                .withCommand("redis-server --port 6378")
                .withExposedPorts(6378);
        REDIS_CONTAINER.start();
    }

    public static GenericContainer<?> getContainer() {
        return REDIS_CONTAINER;
    }
}
