plugins {
    kotlin("jvm") version "1.8.10"
    kotlin("plugin.serialization") version "1.8.10"

    id("net.mamoe.mirai-console") version "2.15.0-RC"
    id("me.him188.maven-central-publish") version "1.0.0-dev-3"
    id("me.him188.kotlin-jvm-blocking-bridge") version "2.2.0-172.1"
}

group = "xyz.cssxsh.mirai"
version = "1.1.2"

mavenCentralPublish {
    useCentralS01()
    singleDevGithubProject("cssxsh", "mirai-tts-plugin")
    licenseFromGitHubProject("AGPL-3.0")
    workingDir = System.getenv("PUBLICATION_TEMP")?.let { file(it).resolve(projectName) }
        ?: buildDir.resolve("publishing-tmp")
    publication {
        artifact(tasks["buildPlugin"])
    }
}

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    api("xyz.cssxsh.baidu:baidu-aip:3.3.2")
    compileOnly("net.mamoe:mirai-silk-converter:0.0.5")
    testImplementation(kotlin("test"))
    testImplementation("net.mamoe:mirai-silk-converter:0.0.5")
    //
    implementation(platform("net.mamoe:mirai-bom:2.15.0-RC"))
    testImplementation("net.mamoe:mirai-core-utils")
    testImplementation("net.mamoe:mirai-console-compiler-common")
    testImplementation("net.mamoe:mirai-logging-slf4j")
    //
    implementation(platform("org.slf4j:slf4j-parent:2.0.7"))
    testImplementation("org.slf4j:slf4j-simple")
}

kotlin {
    explicitApi()
}

mirai {
    jvmTarget = JavaVersion.VERSION_11
}

tasks {
    test {
        useJUnitPlatform()
    }
}