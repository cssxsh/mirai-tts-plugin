package xyz.cssxsh.mirai.tts.command

import net.mamoe.mirai.console.command.*
import net.mamoe.mirai.contact.*
import net.mamoe.mirai.message.data.*
import net.mamoe.mirai.utils.ExternalResource.Companion.toExternalResource
import xyz.cssxsh.mirai.tts.*
import xyz.cssxsh.mirai.tts.data.TextToSpeechConfig

public object TextToSpeechCommand : CompositeCommand(
    owner = MiraiTextToSpeechPlugin,
    "tts",
    description = "TextToSpeech 测试指令"
) {

    @SubCommand
    @Description("测试 tts ")
    public suspend fun CommandSenderOnMessage<*>.test() {
        val receiver = subject as? AudioSupported ?: return

        val text = fromEvent.message.contentToString().substringAfter('\n')

        val audio = try {
            MiraiTextToSpeech.speech(text = text)
                .toExternalResource()
                .use { receiver.uploadAudio(it) }
        } catch (cause: Throwable) {
            logger.error(cause)
            (cause.message ?: cause.toString()).toPlainText()
        }

        sendMessage(audio)
    }

    @SubCommand
    @Description("重新载入 aip 配置")
    public suspend fun CommandSender.reload() {
        with(MiraiTextToSpeechPlugin) {
            TextToSpeechConfig.reload()
        }
        sendMessage("重载完成，当前的 app 为 ${TextToSpeechConfig.appId} - ${TextToSpeechConfig.appName}")
    }
}