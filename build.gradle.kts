import com.github.davidmc24.gradle.plugin.avro.GenerateAvroJavaTask

plugins {
	java
	alias(libs.plugins.springframework.boot)
	alias(libs.plugins.spring.dependency.management)
	alias(libs.plugins.spotless)
	alias(libs.plugins.flyway)
	alias(libs.plugins.avro)
}

group = "dev.notyouraverage"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(24)
	}
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
	maven {
		url = uri("https://packages.confluent.io/maven/")
	}
}

dependencies {
	implementation(libs.spring.boot.starter.web)
	implementation(libs.spring.boot.starter.actuator)
	implementation(libs.spring.boot.starter.validation)
	implementation(libs.spring.boot.starter.data.jpa)
	implementation(libs.spring.boot.starter.data.mongodb)

	implementation(libs.flyway.core)
	implementation(libs.spring.kafka)
	implementation(libs.kafka.avro.serializer)
	implementation(libs.springdoc.openapi.starter.webmvc.ui)

	developmentOnly(libs.spring.boot.devtools)
	developmentOnly(libs.spring.boot.docker.compose)

	compileOnly(libs.lombok)

	runtimeOnly(libs.postgresql)
	runtimeOnly(libs.flyway.database.postgresql)

	annotationProcessor(libs.lombok)
	annotationProcessor(libs.spring.boot.configuration.processor)

	testImplementation(libs.spring.boot.starter.test)
	testImplementation(libs.spring.kafka.test)
	testRuntimeOnly(libs.junit.platform.launcher)
}

tasks.withType<Test> {
	useJUnitPlatform()
}

spotless {
	java {
		importOrder()
		removeUnusedImports()
		eclipse("4.35").configFile("spotless.xml")
		formatAnnotations()
		trimTrailingWhitespace()
		endWithNewline()
	}
}

tasks.named<GenerateAvroJavaTask>("generateAvroJava") {
	setOutputDir(file("src/main/java"))
}

tasks.named("spotlessJava") {
	dependsOn("generateAvroJava")
}
