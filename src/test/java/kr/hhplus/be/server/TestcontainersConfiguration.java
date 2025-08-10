package kr.hhplus.be.server;

import org.springframework.boot.test.context.TestConfiguration;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration
class TestcontainersConfiguration {
	static final MySQLContainer<?> MYSQL = new MySQLContainer<>(DockerImageName.parse("mysql:8.0"))
			.withDatabaseName("hhplus")
			.withUsername("test")
			.withPassword("test")
			.withCommand("--lower-case-table-names=1")
			.withInitScript("schema.sql")
			.withReuse(true);

	static final GenericContainer<?> REDIS = new GenericContainer<>(DockerImageName.parse("redis:8.2.0-alpine"))
			.withExposedPorts(6379)
			.withCommand("redis-server --appendonly no")
			.withReuse(true);

	static {
		MYSQL.start();
		REDIS.start();
	}
}