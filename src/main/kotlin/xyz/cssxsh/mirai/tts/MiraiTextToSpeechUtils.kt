package xyz.cssxsh.mirai.tts

import net.mamoe.mirai.utils.*


internal val logger by lazy {
    try {
        MiraiTextToSpeechPlugin.logger
    } catch (_: Throwable) {
        MiraiLogger.Factory.create(MiraiTextToSpeech::class)
    }
}