package xyz.cssxsh.mirai.tts.command

import io.ktor.utils.io.errors.*
import net.mamoe.mirai.console.command.*
import net.mamoe.mirai.contact.*
import net.mamoe.mirai.message.data.*
import net.mamoe.mirai.utils.ExternalResource.Companion.toExternalResource
import xyz.cssxsh.mirai.tts.*
import xyz.cssxsh.mirai.tts.data.*

/**
 * TextToSpeech 测试指令
 */
public object TextToSpeechCommand : CompositeCommand(
    owner = MiraiTextToSpeechPlugin,
    "tts",
    description = "TextToSpeech 测试指令"
) {

    /**
     * 测试一段文本
     * @param person 音库
     * @param speed 语速
     * @param pitch 语调
     * @param volume 音量
     */
    @SubCommand
    @Description("测试 tts")
    public suspend fun CommandSenderOnMessage<*>.test(person: Int, speed: Int, pitch: Int, volume: Int) {
        val receiver = subject as? AudioSupported ?: return

        val text = fromEvent.message.contentToString().substringAfter('\n')

        val audio = try {
            MiraiTextToSpeech.speech(text = text) {
                this.person = person
                this.speed = speed
                this.pitch = pitch
                this.volume = volume
            }
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
     * 在修改配置文件之后，需要重新载入 aip 配置
     */
    @SubCommand
    @Description("重新载入 aip 配置")
    public suspend fun CommandSender.reload() {
        with(MiraiTextToSpeechPlugin) {
            TextToSpeechConfig.reload()
        }
        sendMessage("重载完成，当前的 app 为 ${TextToSpeechConfig.appId} - ${TextToSpeechConfig.appName}")
    }
}