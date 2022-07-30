# [Mirai TTS Plugin](https://github.com/cssxsh/mirai-tts-plugin)

> Mirai TTS 前置插件

Mirai-Console的前置插件，用于将文本转换成语音等  

[![maven-central](https://img.shields.io/maven-central/v/xyz.cssxsh.mirai/mirai-tts-plugin)](https://search.maven.org/artifact/xyz.cssxsh.mirai/mirai-tts-plugin)
[![Build](https://github.com/cssxsh/mirai-tts-plugin/actions/workflows/build.yml/badge.svg?branch=master)](https://github.com/cssxsh/mirai-tts-plugin/actions/workflows/build.yml)

本插件用的是百度的 [api]((https://ai.baidu.com/ai-doc/SPEECH/Gk38y8lzk)), 返回的是 mp3 格式的语音，非手机端可能播放不不正常,  
你可以安装 [Mirai Silk Converter](https://github.com/project-mirai/mirai-silk-converter) 进行自动转码。  
没有配置 AIP APP 信息时，将使用百度翻译的接口。  
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
* `/tts reload` 重载 aip 配置

## 配置

### [TextToSpeech.yml](src/main/kotlin/xyz/cssxsh/mirai/tts/data/TextToSpeechConfig.kt)

* `app_name` AIP 配置
* `app_id` AIP 配置
* `api_key` AIP 配置
* `secret_key` AIP 配置
* `option` Speech 配置
