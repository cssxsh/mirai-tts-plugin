package xyz.cssxsh.mirai.tts.command

import io.ktor.utils.io.errors.*
import net.mamoe.mirai.console.command.*
import net.mamoe.mirai.contact.*
import net.mamoe.mirai.message.data.*
import net.mamoe.mirai.utils.ExternalResource.Companion.toExternalResource
import xyz.cssxsh.mirai.tts.*
import xyz.cssxsh.mirai.tts.data.*

/**
 * Vits 测试指令
 */
public object VitsCommand : CompositeCommand(
    owner = MiraiTextToSpeechPlugin,
    "vits",
    description = "Vits 测试指令"
) {

    /**
     * 测试一段文本
     * @param model 模型
     * @param speaker 音库
     */
    @SubCommand
    @Description("测试 moegoe")
    public suspend fun CommandSenderOnMessage<*>.moe(model: String, speaker: String) {
        val receiver = subject as? AudioSupported ?: return

        val text = fromEvent.message.contentToString().substringAfter('\n')

        val audio = try {
            MiraiVits.moe(uuid = model)
                .use { vits -> vits.tts(text = text, speaker = speaker) }
                .toExternalResource()
                .use { receiver.uploadAudio(it) }
        } catch (cause: IOException) {
            logger.error("上传语音失败", cause)
            (cause.message ?: cause.toString()).toPlainText()
        } catch (cause: IllegalStateException) {
            logger.error("生成语音失败", cause)
            (cause.message ?: cause.toString()).toPlainText()
        }

        sendMessage(audio)
    }

    /**
     * 在修改配置文件之后，需要重新载入 Vits 配置
     */
    @SubCommand
    @Description("重新载入 Vits 配置")
    public suspend fun CommandSender.reload() {
        with(MiraiTextToSpeechPlugin) {
            VitsConfig.reload()
        }
        sendMessage("重载完成")
    }
}