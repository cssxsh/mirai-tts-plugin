package xyz.cssxsh.mirai.vits

import kotlinx.coroutines.*
import kotlinx.coroutines.sync.*
import kotlinx.serialization.json.*
import org.slf4j.*
import java.io.*
import java.nio.charset.*
import kotlin.coroutines.*

public open class MoeGoe(private val model: String, private val config: String) : CoroutineScope, Closeable {
    private val logger: Logger = LoggerFactory.getLogger(this::class.java)
    private val mutex: Mutex = Mutex(true)
    override val coroutineContext: CoroutineContext = EmptyCoroutineContext
    private val process: Process
    public val speakers: List<String>

    init {
        logger.debug("init...")
        val binary = System.getProperty("xyz.cssxsh.mirai.vits.moggoe.home", "MoeGoe")
        process = ProcessBuilder(binary)
            .redirectErrorStream(true)
            .start()
        speakers = Json.parseToJsonElement(File(config).readText())
            .jsonObject
            .getValue("speakers").jsonArray
            .map { it.jsonPrimitive.content }
        logger.debug("speakers: $speakers")
        launch {
            val reader = process.inputStream.reader()
            val charsetName = System.getProperty("xyz.cssxsh.mirai.vits.moggoe.charset", "GBK")
            val writer = process.outputStream.writer(Charset.forName(charsetName))
            val buffer = CharArray(128)

            while (isActive) {
                val ready = runInterruptible(Dispatchers.IO) {
                    reader.ready()
                }
                if (ready) break
                delay(1000)
                check(process.isAlive)
            }

            // Path of a VITS model:
            logger.debug("model...")
            runInterruptible(Dispatchers.IO) {
                val hits = buffer.concatToString(0, reader.read(buffer))
                logger.trace(hits)
                writer.append(model)
                writer.append('\n')
                writer.flush()
                logger.trace(model)
            }

            // Path of a config file:
            logger.debug("file...")
            runInterruptible(Dispatchers.IO) {
                val hits = buffer.concatToString(0, reader.read(buffer))
                logger.trace(hits)
                writer.append(config)
                writer.append('\n')
                writer.flush()
                logger.trace(config)
            }
        }.invokeOnCompletion { cause ->
            if (cause != null) logger.warn("init failure!", cause) else logger.debug("init success!")
            mutex.unlock()
        }
    }

    override fun close() {
        process.destroy()
        mutex.unlock()
    }

    public suspend fun tts(text: String, id: Int): File {
        return mutex.withLock {
            check(process.isAlive)
            val reader = process.inputStream.reader()
            val writer = process.outputStream.writer(Charset.forName("GBK"))
            val buffer = CharArray(128)

            // TTS or VC? (t/v):
            logger.debug("tts...")
            runInterruptible(Dispatchers.IO) {
                val hits = buffer.concatToString(0, reader.read(buffer))
                logger.trace(hits)
                writer.write("t")
                writer.append('\n')
                writer.flush()
                logger.trace("t")
            }

            // Text to read:
            logger.debug("text...")
            runInterruptible(Dispatchers.IO) {
                val hits = buffer.concatToString(0, reader.read(buffer))
                logger.trace(hits)
                writer.write(text)
                writer.append('\n')
                writer.flush()
                logger.trace(text)
            }

            // Speaker ID:
            logger.debug("speaker...")
            runInterruptible(Dispatchers.IO) {
                val hits = buffer.concatToString(0, reader.read(buffer))
                logger.trace(hits)
                writer.write("$id")
                writer.append('\n')
                writer.flush()
                logger.trace("$id")
            }

            // Path to save:
            logger.debug("save...")
            val temp = java.nio.file.Files.createTempFile("$id-", ".wav").toFile()
            runInterruptible(Dispatchers.IO) {
                val hits = buffer.concatToString(0, reader.read(buffer))
                logger.trace(hits)
                temp.deleteOnExit()
                writer.write(temp.path)
                writer.append('\n')
                writer.flush()
                logger.trace(temp.path)
            }

            // Continue? (y/n):
            logger.debug("continue...")
            runInterruptible(Dispatchers.IO) {
                val hits = buffer.concatToString(0, reader.read(buffer))
                logger.trace(hits)
                writer.write("y")
                writer.append('\n')
                writer.flush()
                logger.trace("y")
            }

            temp
        }
    }

    public suspend fun tts(text: String, speaker: String): File {
        return tts(text = text, id = speakers.indexOf(speaker))
    }
}