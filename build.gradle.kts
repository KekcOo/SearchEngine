plugins {
	java
	id("org.springframework.boot") version "3.4.1"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
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
		url = uri("https://gitlab.skillbox.ru/api/v4/projects/263574/packages/maven")
		credentials(HttpHeaderCredentials::class) {
			name = "Private-Token"
			value = System.getenv("Private-Token")
		}
		authentication {
			create<HttpHeaderAuthentication>("header")
		}
	}
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.jsoup:jsoup:1.16.1")
	compileOnly("org.projectlombok:lombok")
	runtimeOnly("org.postgresql:postgresql")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	implementation("org.apache.lucene.morphology:morph:1.5")
	implementation("org.apache.lucene.analysis:morphology:1.5")
	implementation("org.apache.lucene.morphology:dictionary-reader:1.5")
	implementation("org.apache.lucene.morphology:english:1.5")
	implementation("org.apache.lucene.morphology:russian:1.5")

}

tasks.withType<Test> {
	useJUnitPlatform()
}
