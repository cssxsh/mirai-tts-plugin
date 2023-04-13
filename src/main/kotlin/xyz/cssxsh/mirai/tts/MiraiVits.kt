package xyz.cssxsh.mirai.tts

import io.ktor.client.utils.*
import kotlinx.coroutines.*
import xyz.cssxsh.mirai.vits.*
import java.io.File
import kotlin.coroutines.*

public object MiraiVits : CoroutineScope {

    init {
        System.setProperty("silk-codec.rate-check-ignore", "true")
    }

    override val coroutineContext: CoroutineContext by lazy {
        try {
            MiraiTextToSpeechPlugin.coroutineContext
        } catch (_: UninitializedPropertyAccessException) {
            CoroutineExceptionHandler { _, throwable ->
                if (throwable.unwrapCancellationException() !is CancellationException) {
                    logger.error("Exception in coroutine MiraiVits", throwable)
                }
            }
        } + CoroutineName("MiraiVits")
    }

    private val folder by lazy {
        try {
            MiraiTextToSpeechPlugin.resolveDataFile("vits")
        } catch (_: UninitializedPropertyAccessException) {
            File("debug-sandbox")
        }.apply { mkdirs() }
    }

    @Synchronized
    public fun moe(uuid: String): MoeGoe {
        val target = folder.resolve(uuid)
        val model = target.resolve("G_latest.pth").path
        val config = target.resolve("moegoe_config.json").path
        return object : MoeGoe(model = model, config = config) {
            override val coroutineContext: CoroutineContext get() = this@MiraiVits.coroutineContext
        }
    }
}