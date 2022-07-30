package xyz.cssxsh.mirai.tts

import io.ktor.utils.io.errors.*
import kotlinx.coroutines.*
import me.him188.kotlin.jvm.blocking.bridge.*
import net.mamoe.mirai.utils.*
import xyz.cssxsh.baidu.aip.*
import xyz.cssxsh.baidu.aip.tts.*
import xyz.cssxsh.mirai.tts.data.*
import xyz.cssxsh.mirai.tts.tools.*
import kotlin.coroutines.*

public object MiraiTextToSpeech {
    private val client = object : BaiduAipClient(config = TextToSpeechConfig), CoroutineScope {

        override val coroutineContext: CoroutineContext by lazy {
            try {
                MiraiTextToSpeechPlugin.childScopeContext("BaiduAipContentCensor")
            } catch (_: Throwable) {
                CoroutineExceptionHandler { _, throwable ->
                    if (throwable.unwrapCancellationException() !is CancellationException) {
                        logger.error("Exception in coroutine BaiduAipContentCensor", throwable)
                    }
                }.childScopeContext("BaiduAipContentCensor")
            }
        }

        override val status get() = TextToSpeechToken

        override val apiIgnore: suspend (Throwable) -> Boolean = { throwable ->
            when (throwable) {
                is IOException
                -> {
                    logger.warning { "AipContentCensor Ignore: $throwable" }
                    true
                }

                else -> false
            }
        }
    }
    private val aip = AipTextToSpeech(client = client)
    private val translate = TranslateTextToSpeech(client = client)

    public val ready: Boolean get() = TextToSpeechConfig.secretKey.isNotEmpty()

    public val default: SpeechOption get() = TextToSpeechConfig.option

    @JvmBlockingBridge
    @JvmOverloads
    public suspend fun speech(text: String, block: SpeechOption.() -> Unit = {}): ByteArray {
        val option = aip.default.copy().apply(block)
        return if (ready) {
            aip.handle(text = text, option = option)
        } else {
            translate.handle(text = text, option = option)
        }
    }
}