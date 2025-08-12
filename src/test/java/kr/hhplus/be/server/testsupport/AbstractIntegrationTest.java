package kr.hhplus.be.server.testsupport;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.TestInstance;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;

@Tag("integrationTest")
@Sql(value = "classpath:sql/truncate-all.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AbstractIntegrationTest {
    static {
        TestcontainersConfiguration.MYSQL.isRunning();
        TestcontainersConfiguration.REDIS.isRunning();
    }
    @DynamicPropertySource
    static void registerProps(DynamicPropertyRegistry r) {
        // MySQL
        r.add("spring.datasource.url",
                () -> TestcontainersConfiguration.MYSQL.getJdbcUrl() + "?characterEncoding=UTF-8&serverTimezone=UTC");
        r.add("spring.datasource.username", TestcontainersConfiguration.MYSQL::getUsername);
        r.add("spring.datasource.password", TestcontainersConfiguration.MYSQL::getPassword);

        // Redis
        r.add("spring.data.redis.host", TestcontainersConfiguration.REDIS::getHost);
        r.add("spring.data.redis.port", () -> TestcontainersConfiguration.REDIS.getMappedPort(6379));
    }
}
