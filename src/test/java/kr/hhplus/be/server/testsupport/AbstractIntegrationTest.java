package kr.hhplus.be.server.testsupport;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;

@ActiveProfiles("test")
@Tag("integrationTest")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(value = "classpath:sql/truncate-all.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Import({MySQLTestContainerConfig.class, RedisTestContainerConfig.class})
public class AbstractIntegrationTest {
    static GenericContainer<?> redisContainer = RedisTestContainerConfig.getContainer();
    static MySQLContainer<?> mysqlContainer = MySQLTestContainerConfig.getContainer();

    @DynamicPropertySource
    static void registerProps(DynamicPropertyRegistry r) {
        r.add("spring.datasource.url", mysqlContainer::getJdbcUrl);
        r.add("spring.datasource.username", mysqlContainer::getUsername);
        r.add("spring.datasource.password", mysqlContainer::getPassword);

        String host = redisContainer.getHost();
        Integer port = redisContainer.getMappedPort(6378);
        r.add("spring.data.redis.host", redisContainer::getHost);
        r.add("spring.data.redis.port", () -> port);
        r.add("spring.redis.redisson.config", () ->
                "singleServerConfig:\n" +
                        "  address: \"redis://" + host + ":" + port + "\"\n" +
                        "  database: 0\n" +
                        "  timeout: 3000\n" +
                        "  connectionMinimumIdleSize: 2\n" +
                        "  connectionPoolSize: 8\n" +
                        "threads: 0\n" +
                        "nettyThreads: 0\n"
        );
    }
}
