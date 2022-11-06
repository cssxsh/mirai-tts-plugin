package xyz.cssxsh.mirai.tts.data

import net.mamoe.mirai.console.data.*
import xyz.cssxsh.baidu.aip.tts.*
import xyz.cssxsh.baidu.oauth.*

internal object TextToSpeechConfig : ReadOnlyPluginConfig("TextToSpeech"), BaiduAuthConfig {
    @ValueName("app_name")
    @ValueDescription("百度AI客户端 APP_NAME")
    override val appName: String by value(System.getProperty("xyz.cssxsh.mirai.tts.name", ""))

    @ValueName("app_id")
    @ValueDescription("百度AI客户端 APP_ID")
    override val appId: Long by value(System.getProperty("xyz.cssxsh.mirai.tts.id", "0").toLong())

    @ValueName("api_key")
    @ValueDescription("百度AI客户端 API_KEY")
    override val appKey: String by value(System.getProperty("xyz.cssxsh.mirai.tts.key", ""))

    @ValueName("secret_key")
    @ValueDescription("百度AI客户端 SECRET_KEY")
    override val secretKey: String by value(System.getProperty("xyz.cssxsh.mirai.tts.secret", ""))

    @ValueName("option")
    @ValueDescription("TTS 选项")
    val option: SpeechOption by value(SpeechOption(person = SpeechPerson.Base.MatureFemale))
}