package xyz.cssxsh.mirai.tts

import kotlinx.coroutines.runBlocking
import net.mamoe.mirai.silkconverter.SilkConverter
import net.mamoe.mirai.spi.AudioToSilkService
import net.mamoe.mirai.utils.ExternalResource.Companion.toExternalResource
import net.mamoe.mirai.utils.MiraiExperimentalApi
import org.junit.jupiter.api.Test
import java.io.File

@OptIn(MiraiExperimentalApi::class)
internal class MiraiTextToSpeechTest {

    init {
        AudioToSilkService.setService(SilkConverter())
        File("./test").mkdirs()
    }

    @Test
    fun speech(): Unit = runBlocking {
        File("./test/temp.mp3")
            .writeBytes(MiraiTextToSpeech.speech(text = "阿"))

        val res = File("./test/temp.mp3")
            .toExternalResource()
            .use { AudioToSilkService.convert(it) }

        res.use {
            File("./test/temp.silk").writeBytes(it.inputStream().readAllBytes())
        }
    }
}