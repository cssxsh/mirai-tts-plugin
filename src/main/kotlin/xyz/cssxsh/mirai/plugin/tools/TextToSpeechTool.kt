package xyz.cssxsh.mirai.plugin.tools

import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.features.*
import io.ktor.client.features.compression.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import io.ktor.network.sockets.*
import io.ktor.utils.io.core.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import okhttp3.internal.http2.StreamResetException
import xyz.cssxsh.mirai.plugin.data.TextToSpeechConfig.cacheDir
import xyz.cssxsh.mirai.plugin.data.TextToSpeechConfig.defaultLanguage
import xyz.cssxsh.mirai.plugin.data.TextToSpeechConfig.defaultSpeech
import xyz.cssxsh.mirai.plugin.data.TextToSpeechConfig.defaultType
import java.io.EOFException
import java.io.File
import java.net.ConnectException
import java.net.UnknownHostException
import java.util.*
import javax.net.ssl.SSLException

object TextToSpeechTool {

    private fun httpClient() = HttpClient(OkHttp) {
        Json {
            serializer = KotlinxSerializer()
            accept(ContentType.Text.Html)
        }
        install(HttpTimeout) {
            socketTimeoutMillis = 10_000
            connectTimeoutMillis = 10_000
            requestTimeoutMillis = 10_000
        }
        BrowserUserAgent()
        ContentEncoding {
            gzip()
            deflate()
            identity()
        }
    }

    private val DEFAULT_IGNORE: (exception: Throwable) -> Boolean = { throwable ->
        when (throwable) {
            is SSLException,
            is EOFException,
            is ConnectException,
            is SocketTimeoutException,
            is HttpRequestTimeoutException,
            is StreamResetException,
            is UnknownHostException,
            -> {
                true
            }
            else -> when (throwable.message) {
                "Required SETTINGS preface not received" -> true
                else -> false
            }
        }
    }

    private suspend fun <T> useHttpClient(
        ignore: (exception: Throwable) -> Boolean = DEFAULT_IGNORE,
        block: suspend (HttpClient) -> T
    ): T = httpClient().use {
        var result: T? = null
        while (result === null) {
            result = runCatching { block(it) }.onFailure {
                if (ignore(it).not()) throw it
            }.getOrNull()
        }
        result
    }

    private const val BAIDU_GET_TTS = "https://fanyi.baidu.com/gettts"

    private const val BAIDU_LANGUAGE_DETECT = "https://fanyi.baidu.com/langdetect"

    private const val CONVERT_BATCH = "https://s19.aconvert.com/convert/convert-batch.php"

    private const val CONVERT_RESULT = "https://s19.aconvert.com/convert/p3r68-cdx67/"

    enum class SpeechFileType(val format: String) {
        MP3(format = "mp3"),
        AMR(format = "amr"),
        SILK(format = "silk");
    }

    private fun String.encodeBase64() =
        Base64.getUrlEncoder().encodeToString(toByteArray())

    @Serializable
    private data class LanguageDetect(
        @SerialName("error")
        val error: Int,
        @SerialName("msg")
        val message: String,
        @SerialName("lan")
        val language: String
    )

    private suspend fun getBaiduLanguageDetect(text: String): String = useHttpClient { client ->
        client.get<LanguageDetect>(BAIDU_LANGUAGE_DETECT) {
            parameter("query", text)
        }.takeIf { it.message == "success" }?.language ?: defaultLanguage
    }

    private suspend fun getBaiduTTS(text: String, speed: Int, language: String): ByteArray = useHttpClient { client ->
        client.get(BAIDU_GET_TTS) {
            parameter("text", text)
            parameter("spd", speed)
            parameter("lan", language)
            parameter("source", "web")
        }
    }

    private suspend fun getSpeechOfMp3(text: String, speed: Int): ByteArray =
        getBaiduTTS(text = text, speed = speed, language = getBaiduLanguageDetect(text))

    @Serializable
    private data class ConvertResult(
        @SerialName("ext")
        val ext: String,
        @SerialName("filename")
        val filename: String,
        @SerialName("server")
        val server: String,
        @SerialName("state")
        val state: String
    )

    private suspend fun File.convertTo(format: String): ByteArray = useHttpClient { client ->
        client.submitFormWithBinaryData<ConvertResult>(
            url = CONVERT_BATCH,
            formData = formData {
                append(
                    key = "file",
                    filename = name,
                    size = length(),
                    contentType = ContentType.Audio.MPEG
                ) {
                    writeFully(readBytes())
                }
                append(key = "targetformat", value = format)
            }
        ).let { result ->
            check(result.state == "SUCCESS") {
                "转换错误, $absolutePath -> $result"
            }
            client.get(CONVERT_RESULT + result.filename)
        }
    }

    suspend fun getSpeechFile(
        text: String,
        speed: Int = defaultSpeech,
        type: SpeechFileType = defaultType,
        dir: File = cacheDir
    ): File = dir.resolve("${text.encodeBase64()}.$speed.${type.format}").apply {
        if (exists().not()) {
            writeBytes(
                when(type) {
                    SpeechFileType.AMR -> {
                        getSpeechFile(text = text, speed = speed, type = SpeechFileType.MP3, dir = dir).convertTo(type.format)
                    }
                    SpeechFileType.MP3 -> {
                        getSpeechOfMp3(text = text, speed = speed)
                    }
                    SpeechFileType.SILK -> {
                        getSpeechFile(text = text, speed = speed, type = SpeechFileType.MP3, dir = dir).convertTo(type.format)
                    }
                }
            )
        }
    }
}
