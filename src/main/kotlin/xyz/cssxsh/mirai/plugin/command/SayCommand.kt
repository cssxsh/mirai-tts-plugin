package xyz.cssxsh.mirai.plugin.command

import xyz.cssxsh.mirai.plugin.tools.TextToSpeechTool
import net.mamoe.mirai.console.command.CommandSenderOnMessage
import net.mamoe.mirai.console.command.SimpleCommand
import net.mamoe.mirai.console.command.descriptor.ExperimentalCommandDescriptors
import net.mamoe.mirai.console.util.ConsoleExperimentalApi
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.content
import net.mamoe.mirai.utils.ExternalResource.Companion.toExternalResource
import xyz.cssxsh.mirai.plugin.TextToSpeechHelperPlugin
import xyz.cssxsh.mirai.plugin.data.TextToSpeechConfig.cacheDir
import xyz.cssxsh.mirai.plugin.data.TextToSpeechConfig.defaultSpeech
import xyz.cssxsh.mirai.plugin.data.TextToSpeechConfig.defaultType
import xyz.cssxsh.mirai.plugin.data.TextToSpeechConfig.textMaxLength
import java.io.File

object SayCommand : SimpleCommand(
    owner = TextToSpeechHelperPlugin,
    "tts", "say", "说",
    description = "Say指令"
) {

    @ExperimentalCommandDescriptors
    @ConsoleExperimentalApi
    override val prefixOptional: Boolean = true

    private fun Message.getSpeechText() =
        content.replace("""^(/)?(tts|say|说)""".toRegex(), "").trim().takeIf { it.length <= textMaxLength } ?: "太长不说"

    private suspend fun getSpeechFile(text: String) =
        TextToSpeechTool.getSpeechFile(text = text, speed = defaultSpeech, type = defaultType, dir = cacheDir)

    private suspend fun Group.sendVoice(file: File) =
        sendMessage(file.toExternalResource().use { uploadVoice(it) })

    @ConsoleExperimentalApi
    @Handler
    @Suppress("unused")
    suspend fun CommandSenderOnMessage<GroupMessageEvent>.handle() {
        fromEvent.message.getSpeechText().takeIf { it.isNotEmpty() }?.let { text ->
            fromEvent.group.sendVoice(getSpeechFile(text))
        }
    }
}