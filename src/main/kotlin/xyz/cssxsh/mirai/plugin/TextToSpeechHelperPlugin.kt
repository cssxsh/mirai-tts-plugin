package xyz.cssxsh.mirai.plugin

import net.mamoe.mirai.console.command.CommandManager.INSTANCE.register
import net.mamoe.mirai.console.command.CommandManager.INSTANCE.unregister
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.console.util.ConsoleExperimentalApi
import xyz.cssxsh.mirai.plugin.data.*
import xyz.cssxsh.mirai.plugin.command.*
import kotlin.time.*

object TextToSpeechHelperPlugin : KotlinPlugin(
    JvmPluginDescription("xyz.cssxsh.mirai.plugin.tts-helper", "0.1.0-dev-1") {
        name("tts-helper")
        author("cssxsh")
    }
) {
    @ConsoleExperimentalApi
    override val autoSaveIntervalMillis: LongRange
        get() = (10).minutes.toLongMilliseconds()..(30).minutes.toLongMilliseconds()

    override fun onEnable() {
        TextToSpeechConfig.reload()
        TextToSpeechConfig.cacheDir.mkdirs()
        SayCommand.register()
    }

    override fun onDisable() {
        SayCommand.unregister()
    }
}