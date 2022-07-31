package xyz.cssxsh.mirai.tts.tools

import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.serialization.*
import xyz.cssxsh.baidu.aip.tts.*
import xyz.cssxsh.baidu.api.*

public class TranslateTextToSpeech(public val client: BaiduApiClient<*>) {
    public companion object {
        private const val BAIDU_GET_TTS = "https://fanyi.baidu.com/gettts"

        private const val BAIDU_LANGUAGE_DETECT = "https://fanyi.baidu.com/langdetect"

        private const val DEFAULT_LANGUAGE = "zh"
    }

    @Serializable
    private data class LanguageDetect(
        @SerialName("error")
        val error: Int,
        @SerialName("msg")
        val message: String,
        @SerialName("lan")
        val language: String = DEFAULT_LANGUAGE
    )

    /**
     * 获取文本语言
     */
    public suspend fun detect(text: String): String {
        return client.useHttpClient { http ->
            http.get(BAIDU_LANGUAGE_DETECT) {
                parameter("query", text)
            }.body<LanguageDetect>().language
        }
    }

    /**
     * 从百度翻译 api 获取 tts，格式mp3
     */
    public suspend fun handle(text: String, option: SpeechOption, language: String = DEFAULT_LANGUAGE): ByteArray {
        return client.useHttpClient { http ->
            http.get(BAIDU_GET_TTS) {
                parameter("text", text)
                parameter("source", "web")
                parameter("lan", language)
                parameter("spd", option.speed)
                parameter("pit", option.pitch)
                parameter("vol", option.volume)
                parameter("per", option.person)
                parameter("aue", option.format)
            }.body()
        }
    }
}
