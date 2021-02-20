package xyz.cssxsh.mirai.plugin.data

import net.mamoe.mirai.console.data.ReadOnlyPluginConfig
import net.mamoe.mirai.console.data.ValueName
import net.mamoe.mirai.console.data.value
import xyz.cssxsh.mirai.plugin.tools.TextToSpeechTool.SpeechFileType
import java.io.File

object TextToSpeechConfig : ReadOnlyPluginConfig("TextToSpeechConfig") {

    @ValueName("cache_path")
    private val cachePath: String by value("TextToSpeechCache")

    val cacheDir get() = File(cachePath)

    @ValueName("text_max_length")
    val textMaxLength: Int by value(256)

    @ValueName("default_language")
    val defaultLanguage: String by value("zh")

    @ValueName("default_speech")
    val defaultSpeech: Int by value(5)

    @ValueName("default_type")
    val defaultType: SpeechFileType by value(SpeechFileType.MP3)
}