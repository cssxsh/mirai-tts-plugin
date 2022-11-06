package xyz.cssxsh.mirai.tts

import io.ktor.client.utils.*
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

/**
 * TTS 实例
 */
public object MiraiTextToSpeech : BaiduAipClient(config = TextToSpeechConfig), CoroutineScope {

    override val coroutineContext: CoroutineContext by lazy {
        try {
            MiraiTextToSpeechPlugin.coroutineContext + CoroutineName("MiraiTextToSpeech")
        } catch (_: ExceptionInInitializerError) {
            CoroutineExceptionHandler { _, throwable ->
                if (throwable.unwrapCancellationException() !is CancellationException) {
                    logger.error("Exception in coroutine MiraiTextToSpeech", throwable)
                }
            } + CoroutineName("MiraiTextToSpeech")
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

    /**
     * 是否已配置 App Key 等信息
     * @see TextToSpeechConfig
     */
    public val ready: Boolean get() = TextToSpeechConfig.secretKey.isNotEmpty()

    /**
     * 默认语音选项设置
     * @see TextToSpeechConfig
     */
    public val default: SpeechOption get() = TextToSpeechConfig.option

    /**
     * 讲述一段文本
     * @param text 文本
     * @param option 选项
     */
    @JvmBlockingBridge
    public suspend fun speech(text: String, option: SpeechOption): ByteArray {
        return if (ready) {
            aip.handle(text = text, option = option)
        } else {
            free.handle(text = text, option = option)
        }
    }

    /**
     * 讲述一段文本
     * @param text 文本
     */
    @JvmBlockingBridge
    public suspend fun speech(text: String): ByteArray {
        return speech(text = text, option = default)
    }

    /**
     * 讲述一段文本
     * @param text 文本
     * @param block 选项DSL
     */
    @JvmBlockingBridge
    public suspend fun speech(text: String, block: SpeechOption.() -> Unit): ByteArray {
        return speech(text = text, option = default.copy().apply(block))
    }
}