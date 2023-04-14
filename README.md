# [Mirai TTS Plugin](https://github.com/cssxsh/mirai-tts-plugin)

> Mirai TTS 前置插件

Mirai-Console的前置插件，用于将文本转换成语音等  

[![maven-central](https://img.shields.io/maven-central/v/xyz.cssxsh.mirai/mirai-tts-plugin)](https://search.maven.org/artifact/xyz.cssxsh.mirai/mirai-tts-plugin)
[![Build](https://github.com/cssxsh/mirai-tts-plugin/actions/workflows/build.yml/badge.svg?branch=master)](https://github.com/cssxsh/mirai-tts-plugin/actions/workflows/build.yml)
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/c10823fade1b4a6580ffb08a777c75f0)](https://www.codacy.com/gh/cssxsh/mirai-tts-plugin/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=cssxsh/mirai-tts-plugin&amp;utm_campaign=Badge_Grade)

本插件使用的TTS API 返回的是 mp3 格式的语音，非手机端接收到语音可能播放不正常,  
你可以安装 [Mirai Silk Converter](https://github.com/project-mirai/mirai-silk-converter) 进行自动转码，解决mp3格式的问题。  
没有配置 AIP APP 信息时，将使用百度百科的接口，此接口只支持部分 `person` 。  
如需要更多的功能选择，请到 [baidu aip](https://ai.baidu.com/ai-doc/SPEECH/qknh9i8ed#%E6%88%90%E4%B8%BA%E5%BC%80%E5%8F%91%E8%80%85)  申请新应用和免费额度。

本插件不提供下载 `Vits` 模型的支持！！！也不接受如何下载或训练的询问！！！

## 在插件项目中引用

`build.gradle.kt` 示例
```kotlin
repositories {
    mavenCentral()
}

dependencies {
    compileOnly("xyz.cssxsh.mirai:mirai-tts-plugin:${version}")
}

mirai {
    jvmTarget = JavaVersion.VERSION_11
}
```

## TTS指令

*   `/tts test <person> <speed> <pitch> <volume>` 测试 tts
    ```log
    /tts test 4100 5 5 5
    你好 世界
    ```

*   `/tts reload` 重载 aip 配置

## VITS指令

*   `/vits moe <model> <speaker>`
    ```log
    /vits moe arknights 阿米娅
    [JA] はあなたが大好きです [JA]
    ```
    
*   `/vits reload` 重载 Vits 配置

## 配置

### [TextToSpeech.yml](src/main/kotlin/xyz/cssxsh/mirai/tts/data/TextToSpeechConfig.kt)

AIP 应用配置信息 可以在 [百度云控制台](https://console.bce.baidu.com/ai/?fromai=1#/ai/speech/app/list) 找到

*   `app_name` AIP 配置 应用名称

*   `app_id` AIP 配置 AppID

*   `api_key` AIP 配置 API Key

*   `secret_key` AIP 配置 Secret Key

*   `option` Speech 配置
    *   `speed` 语速 0~15
    *   `pitch` 语调 0~15
    *   `volume` 音量 0~15
    *   `person` 音库 详见 [demo](example/demo.json), 百度百科的接口只支持 `0, 1, 3, 106, 4100, 4106`
    *   `format` 格式

### [VitsConfig.yml](src/main/kotlin/xyz/cssxsh/mirai/tts/data/VitsConfig.kt)

Vits 配置信息, 其中 [MoeGoe](https://github.com/CjangCjengh/MoeGoe/releases/latest) 请自行下载并解压

*  `moegoe` MoeGoe 启动路径，你需要修改为你自己的目录

模型请放置于 `data\xyz.cssxsh.mirai.plugin.mirai-tts-plugin\vits` 下  
例如：
```log
data
└───xyz.cssxsh.mirai.plugin.mirai-tts-plugin
    └───vits
        ├───arknights
        │   ├───G_latest.pth
        |   └───moegoe_config.json
        └───genshin
            ├───G_latest.pth
            └───moegoe_config.json
```

### 示例代码

*   [kotlin](src/main/kotlin/xyz/cssxsh/mirai/tts/command/TextToSpeechCommand.kt)
*   [java](src/test/java/xyz/cssxsh/mirai/test/MiraiTTSDemo.java)

## 安装

### MCL 指令安装

**请确认 mcl.jar 的版本是 2.1.0+**  
`./mcl --update-package xyz.cssxsh.mirai:mirai-tts-plugin --channel maven-stable --type plugins`

### 手动安装

1.  从 [Releases](https://github.com/cssxsh/mirai-tts-plugin/releases) 或者 [Maven](https://repo1.maven.org/maven2/xyz/cssxsh/mirai/mirai-tts-plugin/) 下载 `mirai2.jar`
2.  将其放入 `plugins` 文件夹中

## [爱发电](https://afdian.net/@cssxsh)

![afdian](.github/afdian.jpg)
