group = "com.serbar"
version = "1.0"
description = "MyWordle"
java.sourceCompatibility = JavaVersion.VERSION_17

plugins {
    java
    application
    `maven-publish`
    id("org.springframework.boot") version "2.6.3"
    id("io.spring.dependency-management") version "1.0.9.RELEASE"
}

apply(plugin = "io.spring.dependency-management")

application {
    mainClass.set("com.serbar.wordle.MyWordleApplication")
}

repositories {
    mavenLocal()
    maven {
        url = uri("https://repo.maven.apache.org/maven2/")
    }
}

dependencies {
    implementation("org.springframework.boot", "spring-boot-starter-parent", prop("spring_boot_ver"))
    implementation("org.springframework.boot", "spring-boot-starter", prop("spring_boot_ver"))
    implementation("org.springframework.boot", "spring-boot-starter-web", prop("spring_boot_ver"))
    implementation("org.springframework.boot", "spring-boot-starter-webflux", prop("spring_boot_ver"))
    implementation("org.springframework.boot", "spring-boot-starter-log4j2", prop("spring_boot_ver"))
    implementation("org.springframework.boot", "spring-boot-starter-cache", prop("spring_boot_ver"))


    implementation("org.springframework.boot", "spring-boot-configuration-processor", prop("spring_boot_ver"))
    implementation("org.springframework.boot", "spring-boot-starter-data-mongodb", prop("spring_boot_ver"))

    //implementation("javax.cache:cache-api:1.1.1")
    //implementation("org.ehcache:ehcache:3.9.6")

    implementation("org.springdoc", "springdoc-openapi-ui", prop("springdoc_ver"))
    implementation("org.springdoc", "springdoc-openapi-data-rest", prop("springdoc_ver"))
    implementation("org.springdoc", "springdoc-openapi-webmvc-core", prop("springdoc_ver"))
    implementation("io.swagger.core.v3:swagger-annotations:2.1.11")


    implementation("com.fasterxml.jackson.core", "jackson-databind", prop("jackson_ver"))
    implementation("com.fasterxml.jackson.core", "jackson-annotations", prop("jackson_ver"))

    testImplementation("org.springframework.boot", "spring-boot-starter-test", prop("spring_boot_ver"))
}

configurations.all {
    exclude("org.springframework.boot", "spring-boot-starter-logging")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.getByName<Jar>("jar") {
    enabled = false
}

tasks.getByName<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
    archiveVersion.set("")
    launchScript()
}

tasks.withType<Test> {
    useJUnitPlatform()
}

fun prop(name: String) = properties[name] as String
