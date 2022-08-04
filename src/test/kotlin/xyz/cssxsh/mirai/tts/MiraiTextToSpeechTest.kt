package xyz.cssxsh.mirai.tts

import kotlinx.coroutines.runBlocking
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import net.mamoe.mirai.silkconverter.SilkConverter
import net.mamoe.mirai.spi.AudioToSilkService
import net.mamoe.mirai.utils.ExternalResource.Companion.toExternalResource
import net.mamoe.mirai.utils.MiraiExperimentalApi
import org.junit.jupiter.api.Test
import xyz.cssxsh.baidu.aip.tts.SpeechException
import java.io.File

@OptIn(MiraiExperimentalApi::class)
internal class MiraiTextToSpeechTest {

    init {
        AudioToSilkService.setService(SilkConverter())
        File("./test").mkdirs()
    }

    private val demo: List<List<DemoExample>> = Json.decodeFromString(File("./example/demo.json").readText())

    @Test
    fun speech(): Unit = runBlocking {

        for (group in demo) {
            for (example in group) {
                val result = try {
                    MiraiTextToSpeech.speech(text = example.defaultText) {
                        person = example.person
                    }
                } catch (exception: SpeechException) {
                    logger.warning("${example.name} 不支持， ${exception.message}")
                    continue
                } catch (cause: Throwable) {
                    logger.error(cause)
                    continue
                }
                val mp3 = File("./test/${example.name}-${example.person}.mp3")
                mp3.writeBytes(result)

                val silk = File("./test/${example.name}-${example.person}.silk")
                try {
                    mp3.toExternalResource()
                        .use { AudioToSilkService.convert(it) }
                        .use { silk.writeBytes(it.inputStream().readAllBytes()) }
                } catch (cause: Throwable) {
                    logger.error(cause)
                    throw cause
                }
            }
        }
    }
}