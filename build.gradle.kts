plugins {
	java
}

fun getGitHash(): String {
	return providers.exec {
		commandLine("git", "rev-parse", "--short", "HEAD")
	}.standardOutput.asText.get().trim()
}

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

allprojects{
	group = "kr.hhplus.be"
	version = getGitHash()
	repositories {
		mavenCentral()
	}
}

subprojects {
	apply (plugin = "java")
}

