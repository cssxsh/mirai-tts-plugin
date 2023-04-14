package xyz.cssxsh.mirai.tts

import net.mamoe.mirai.console.command.CommandManager.INSTANCE.register
import net.mamoe.mirai.console.command.CommandManager.INSTANCE.unregister
import net.mamoe.mirai.console.plugin.jvm.*
import net.mamoe.mirai.utils.*
import xyz.cssxsh.mirai.tts.data.*
import xyz.cssxsh.mirai.tts.command.*

@PublishedApi
internal object MiraiTextToSpeechPlugin : KotlinPlugin(
    JvmPluginDescription("xyz.cssxsh.mirai.plugin.mirai-tts-plugin", "1.1.0") {
        name("mirai-tts-plugin")
        author("cssxsh")

        dependsOn("net.mamoe.mirai-silk-converter", true)
    }
) {
    override fun onEnable() {
        TextToSpeechConfig.reload()
        TextToSpeechToken.reload()
        TextToSpeechCommand.register()

        VitsConfig.reload()
        VitsCommand.register()

        if (!MiraiTextToSpeech.ready) {
            logger.warning { "未配置 aip 信息，将使用百度百科的tts" }
        }
        try {
            @OptIn(MiraiExperimentalApi::class)
            net.mamoe.mirai.silkconverter.SilkConverter::class.java
        } catch (_: NoClassDefFoundError) {
            logger.warning { "未安装 https://github.com/project-mirai/mirai-silk-converter 语音将可能直接按 mp3 发送" }
        }
    }

    override fun onDisable() {
        TextToSpeechCommand.unregister()
        VitsCommand.unregister()
    }
}