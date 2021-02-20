package xyz.cssxsh.mirai.plugin.tools

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import java.io.File

internal class TextToSpeechToolTest {

    private val tool get() = TextToSpeechTool

    private val cache = File("test")

    @Test
    fun getSpeechFileTest(): Unit = runBlocking {
        tool.getSpeechFile("测试", 5, TextToSpeechTool.SpeechFileType.MP3, cache).let {
            println(it.absolutePath)
            println(it.length())
        }
        tool.getSpeechFile("测试", 5, TextToSpeechTool.SpeechFileType.AMR, cache).let {
            println(it.absolutePath)
            println(it.length())
        }
    }
}