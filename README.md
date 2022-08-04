# [Mirai TTS Plugin](https://github.com/cssxsh/mirai-tts-plugin)

> Mirai TTS 前置插件

Mirai-Console的前置插件，用于将文本转换成语音等  

[![maven-central](https://img.shields.io/maven-central/v/xyz.cssxsh.mirai/mirai-tts-plugin)](https://search.maven.org/artifact/xyz.cssxsh.mirai/mirai-tts-plugin)
[![Build](https://github.com/cssxsh/mirai-tts-plugin/actions/workflows/build.yml/badge.svg?branch=master)](https://github.com/cssxsh/mirai-tts-plugin/actions/workflows/build.yml)

本插件使用的TTS API 返回的是 mp3 格式的语音，非手机端接收到语音可能播放不正常,  
你可以安装 [Mirai Silk Converter](https://github.com/project-mirai/mirai-silk-converter) 进行自动转码，解决mp3格式的问题。  
没有配置 AIP APP 信息时，将使用百度百科的接口，此接口部分 `person` 配置无效。  
如需要更多的功能选择，请到 [baidu aip](https://ai.baidu.com/ai-doc/SPEECH/qknh9i8ed#%E6%88%90%E4%B8%BA%E5%BC%80%E5%8F%91%E8%80%85)  申请新应用和免费额度。


## 在插件项目中引用

```
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

## 管理指令

* `/tts test` 测试 tts
    ```
    /tts test
    你好 世界
    ```
* `/tts reload` 重载 aip 配置

## 配置

### [TextToSpeech.yml](src/main/kotlin/xyz/cssxsh/mirai/tts/data/TextToSpeechConfig.kt)

* `app_name` AIP 配置
* `app_id` AIP 配置
* `api_key` AIP 配置
* `secret_key` AIP 配置
* `option` Speech 配置
    * speed: 语速 0~15
    * pitch: 语调 0~15
    * volume: 音量 0~15
    * person: 音库
    * format: 格式
