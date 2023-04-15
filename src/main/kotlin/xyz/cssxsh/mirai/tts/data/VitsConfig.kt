package xyz.cssxsh.mirai.tts.data

import kotlinx.coroutines.*
import net.mamoe.mirai.console.data.*
import net.mamoe.mirai.console.plugin.jvm.*
import net.mamoe.mirai.console.util.*

public object VitsConfig : ReadOnlyPluginConfig("Vits") {

    @ValueName("moegoe")
    @ValueDescription("MoeGoe 启动路径")
    public val moegoe: String by value("D:\\Users\\CSSXSH\\Vits\\MoeGoe\\MoeGoe.exe")

    @ConsoleExperimentalApi
    override fun onInit(owner: PluginDataHolder, storage: PluginDataStorage) {
        super.onInit(owner, storage)

        if (owner is JvmPlugin) {
            owner.launch {
                delay(3_000)
                System.setProperty("xyz.cssxsh.mirai.vits.moegoe", moegoe)
            }
        }
    }
}