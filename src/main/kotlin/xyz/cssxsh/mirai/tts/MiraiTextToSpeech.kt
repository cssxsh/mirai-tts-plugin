package xyz.cssxsh.mirai.tts

import io.ktor.utils.io.errors.*
import kotlinx.coroutines.*
import me.him188.kotlin.jvm.blocking.bridge.*
import net.mamoe.mirai.utils.*
import xyz.cssxsh.baidu.aip.*
import xyz.cssxsh.baidu.aip.tts.*
import xyz.cssxsh.baidu.oauth.*
import xyz.cssxsh.mirai.tts.data.*
import xyz.cssxsh.mirai.tts.tools.*
import kotlin.coroutines.*

public object MiraiTextToSpeech : BaiduAipClient(config = TextToSpeechConfig), CoroutineScope {

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
    override val status: BaiduAuthStatus get() = TextToSpeechToken
    override val apiIgnore: suspend (Throwable) -> Boolean = { throwable ->
        when (throwable) {
            is IOException -> {
                logger.warning { "AipContentCensor Ignore: $throwable" }
                true
            }
            else -> false
        }
    }
    private val aip = AipTextToSpeech(client = this)
    private val free = FreeTextToSpeech(client = this)

    public val ready: Boolean get() = TextToSpeechConfig.secretKey.isNotEmpty()

    public val default: SpeechOption get() = TextToSpeechConfig.option

    @JvmBlockingBridge
    public suspend fun speech(text: String, option: SpeechOption): ByteArray {
        return if (ready) {
            aip.handle(text = text, option = option)
        } else {
            free.handle(text = text, option = option)
        }
    }

    @JvmBlockingBridge
    public suspend fun speech(text: String): ByteArray {
        return speech(text = text, option = default)
    }

    @JvmBlockingBridge
    public suspend fun speech(text: String, block: SpeechOption.() -> Unit): ByteArray {
        return speech(text = text, option = default.copy().apply(block))
    }
}