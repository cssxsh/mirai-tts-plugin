package xyz.cssxsh.mirai.tts.tools

import io.ktor.client.call.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import xyz.cssxsh.baidu.aip.tts.*
import xyz.cssxsh.baidu.api.*

public class FreeTextToSpeech(public val client: BaiduApiClient<*>) {
    public companion object {
        internal const val FREE_TTS = "https://tts.baidu.com/text2audio"
    }

    /**
     * 从百度百科 api 获取 tts，格式mp3
     */
    public suspend fun handle(text: String, option: SpeechOption): ByteArray {
        return client.useHttpClient { http ->
            http.prepareForm(FREE_TTS, Parameters.build {
                append("tex", text)
                append("pdt", "301")
                append("cuid", "bake")
                append("ctp", "1")
                append("lan", "zh")
                append("spd", option.speed.toString())
                append("pit", option.pitch.toString())
                append("vol", option.volume.toString())
                append("per", option.person.toString())
                append("aue", option.format.toString())
            }).execute { response ->
                val type = requireNotNull(response.contentType()) { "ContentType is null." }
                when {
                    type.match(ContentType.Audio.Any) -> response.body()
                    type.match(ContentType.Application.Json) -> throw SpeechException(response, response.body())
                    else -> throw IllegalStateException("ContentType: $type not is audio.")
                }
            }
        }
    }
}
