plugins {
    `java-library`
    id("io.spring.dependency-management") version "1.1.7"
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.boot:spring-boot-dependencies:3.4.1")
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:2024.0.0")
    }
}

dependencies {
    // Spring Boot 공통 의존성
    api("org.springframework.boot:spring-boot-starter")
    api("org.springframework.boot:spring-boot-configuration-processor")
    
    // Test 공통
    api("org.springframework.boot:spring-boot-starter-test")
    api("org.junit.platform:junit-platform-launcher")
}