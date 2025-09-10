plugins {
    id("org.springframework.boot") version "3.4.1"
    id("io.spring.dependency-management") version "1.1.7"
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.boot:spring-boot-dependencies:3.4.1")
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:2024.0.0")
    }
}

dependencies {
    // Modules
    implementation(project(":common"))
    implementation(project(":infrastructure-kafka"))
    
    // Lombok
    compileOnly("org.projectlombok:lombok:1.18.32")
    annotationProcessor("org.projectlombok:lombok:1.18.32")
    testCompileOnly("org.projectlombok:lombok:1.18.32")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.32")
    
    // Spring
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.redisson:redisson-spring-boot-starter:3.27.+")
    implementation("org.springframework.boot:spring-boot-configuration-processor")
    implementation("org.springframework.kafka:spring-kafka")
    testImplementation("org.springframework.kafka:spring-kafka-test")
    
    // Prometheus
    implementation("io.micrometer:micrometer-core")
    implementation("io.micrometer:micrometer-registry-prometheus")

    // DB
    runtimeOnly("com.mysql:mysql-connector-j")

    // Test
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:mysql")
    testImplementation("org.testcontainers:kafka")

    // Swagger
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.9")

    // shedlock
    implementation("net.javacrumbs.shedlock:shedlock-spring:6.3.0")
    implementation("net.javacrumbs.shedlock:shedlock-provider-redis-spring:6.3.0")
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