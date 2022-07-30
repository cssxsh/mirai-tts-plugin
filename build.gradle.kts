plugins {
    kotlin("jvm") version "1.6.21"
    kotlin("plugin.serialization") version "1.6.21"

    id("net.mamoe.mirai-console") version "2.12.0"
    id("net.mamoe.maven-central-publish") version "0.7.1"
    id("me.him188.kotlin-jvm-blocking-bridge") version "2.1.0-162.1"
}

group = "xyz.cssxsh.mirai.plugin"
version = "1.0.0-dev-1"

mavenCentralPublish {
    useCentralS01()
    singleDevGithubProject("cssxsh", "mirai-tts-plugin")
    licenseFromGitHubProject("AGPL-3.0", "master")
    publication {
        artifact(tasks.getByName("buildPlugin"))
    }
}

repositories {
    mavenLocal()
    mavenCentral()
}

kotlin {
    explicitApi()
}

mirai {
    jvmTarget = JavaVersion.VERSION_11
}

dependencies {
    api("xyz.cssxsh.baidu:baidu-aip:3.1.3") {
        exclude(group = "org.jetbrains.kotlin")
        exclude(group = "org.jetbrains.kotlinx")
        exclude(group = "org.slf4j")
    }
    compileOnly("net.mamoe:mirai-core-utils:2.12.0")
    compileOnly("net.mamoe:mirai-silk-converter:0.0.5")
    //
    testImplementation(kotlin("test", "1.6.21"))
    testImplementation("net.mamoe:mirai-silk-converter:0.0.5")
}

tasks {
    test {
        useJUnitPlatform()
    }
}