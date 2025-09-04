plugins {
	java
	id("org.springframework.boot") version "3.4.1"
	id("io.spring.dependency-management") version "1.1.7"
}

fun getGitHash(): String {
	return providers.exec {
		commandLine("git", "rev-parse", "--short", "HEAD")
	}.standardOutput.asText.get().trim()
}

group = "kr.hhplus.be"
version = getGitHash()

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

repositories {
	mavenCentral()
}

dependencyManagement {
	imports {
		mavenBom("org.springframework.cloud:spring-cloud-dependencies:2024.0.0")
	}
}

dependencies {
    // Spring
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-data-redis")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.redisson:redisson-spring-boot-starter:3.27.+")
	implementation("org.springframework.boot:spring-boot-configuration-processor")
	implementation("org.springframework.kafka:spring-kafka")
	testImplementation("org.springframework.kafka:spring-kafka-test")

    // DB
	runtimeOnly("com.mysql:mysql-connector-j")

    // Test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.boot:spring-boot-testcontainers")
	testImplementation("org.testcontainers:junit-jupiter")
	testImplementation("org.testcontainers:mysql")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	testImplementation("org.springframework.kafka:spring-kafka-test")
	testImplementation("org.testcontainers:kafka")

	// Swagger
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.9")

	// Lombok
	compileOnly("org.projectlombok:lombok:1.18.32")
	annotationProcessor("org.projectlombok:lombok:1.18.32")

	// shedlock
	implementation("net.javacrumbs.shedlock:shedlock-spring:6.3.0")
	implementation("net.javacrumbs.shedlock:shedlock-provider-redis-spring:6.3.0")

	testCompileOnly("org.projectlombok:lombok:1.18.32")
	testAnnotationProcessor("org.projectlombok:lombok:1.18.32")
}

tasks.withType<Test> {
	useJUnitPlatform()
	systemProperty("user.timezone", "UTC")
}

tasks.test {
	outputs.upToDateWhen { false }
	useJUnitPlatform {}
}

tasks.register<Test>("unitTest") {
	group = "verification"
	description = "Runs only unit tests"

	val testSourceSet = sourceSets.named("test").get()
	testClassesDirs = testSourceSet.output.classesDirs
	classpath = testSourceSet.runtimeClasspath

	useJUnitPlatform { excludeTags("integrationTest") }

	outputs.upToDateWhen { false }
}

tasks.register<Test>("integrationTest") {
	group = "verification"
	description = "Runs only integration tests"

	val testSourceSet = sourceSets.named("test").get()
	testClassesDirs = testSourceSet.output.classesDirs
	classpath = testSourceSet.runtimeClasspath

	useJUnitPlatform { includeTags("integrationTest") }

	outputs.upToDateWhen { false }
	shouldRunAfter("unitTest")
}
