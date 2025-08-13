package kr.hhplus.be.server.testsupport;

import org.springframework.boot.test.context.TestConfiguration;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration
public class MySQLTestContainerConfig {
    private static MySQLContainer<?> MYSQL_CONTAINER;
    private static final DockerImageName MYSQL_IMAGE = DockerImageName.parse("mysql:8.0");

    static {
        MYSQL_CONTAINER = new MySQLContainer<>(MYSQL_IMAGE)
                .withDatabaseName("hhplus")
                .withUsername("test")
                .withPassword("test")
                .withCommand("--lower-case-table-names=1")
                .withInitScript("schema.sql");
        MYSQL_CONTAINER.start();
    }

    public static MySQLContainer<?> getContainer() {
        return MYSQL_CONTAINER;
    }
}
