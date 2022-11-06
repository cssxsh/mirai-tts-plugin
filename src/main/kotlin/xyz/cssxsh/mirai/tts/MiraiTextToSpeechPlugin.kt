package xyz.cssxsh.mirai.tts

import net.mamoe.mirai.console.command.CommandManager.INSTANCE.register
import net.mamoe.mirai.console.command.CommandManager.INSTANCE.unregister
import net.mamoe.mirai.console.plugin.jvm.*
import net.mamoe.mirai.utils.*
import xyz.cssxsh.mirai.tts.data.*
import xyz.cssxsh.mirai.tts.command.*

@PublishedApi
internal object MiraiTextToSpeechPlugin : KotlinPlugin(
    JvmPluginDescription("xyz.cssxsh.mirai.plugin.mirai-tts-plugin", "1.0.1") {
        name("mirai-tts-plugin")
        author("cssxsh")

        dependsOn("net.mamoe.mirai-silk-converter", true)
    }
) {
    override fun onEnable() {
        TextToSpeechConfig.reload()
        TextToSpeechToken.reload()
        TextToSpeechCommand.register()

        if (!MiraiTextToSpeech.ready) {
            logger.warning { "未配置 aip 信息，将使用百度翻译的tts" }
        }
    }

    override fun onDisable() {
        TextToSpeechCommand.unregister()
    }
}