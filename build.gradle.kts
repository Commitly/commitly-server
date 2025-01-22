plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.4.1"
    id("io.spring.dependency-management") version "1.1.7"
    id ("org.jetbrains.kotlin.plugin.jpa") version "1.9.24"
}

group = "com.leegeonhee"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

repositories {
    mavenCentral()
    maven { url = uri("https://repo.spring.io/milestone") }
}
val springCloudVersion by extra("2023.0.2")
val springAiVersion by extra("1.0.0-M3")
repositories {
    mavenCentral()
    maven { url = uri("https://repo.spring.io/milestone") }
    maven { url = uri("https://repo.spring.io/snapshot") }

}
dependencyManagement {
    imports {
        mavenBom("org.springframework.ai:spring-ai-bom:${springAiVersion}")
    }
}
dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    //
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.7.3")
    implementation("org.springframework.ai:spring-ai-openai-spring-boot-starter")
    implementation (platform("org.springframework.ai:spring-ai-bom:1.0.0-SNAPSHOT"))


    //jwt
    implementation ("org.springframework.boot:spring-boot-starter-security")
    implementation("io.jsonwebtoken:jjwt-api:0.12.5")
    implementation("io.jsonwebtoken:jjwt-impl:0.12.5")
    implementation("io.jsonwebtoken:jjwt-jackson:0.12.5")
    //
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    runtimeOnly("com.mysql:mysql-connector-j")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.3.0")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation ("org.springframework.boot:spring-boot-starter-data-jpa")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
