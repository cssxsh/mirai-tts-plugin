package xyz.cssxsh.mirai.tts.data

import net.mamoe.mirai.console.data.*
import net.mamoe.mirai.console.util.*

public object VitsConfig : ReadOnlyPluginConfig("Vits") {

    @ValueName("moegoe")
    @ValueDescription("MoeGoe 启动路径")
    public val moegoe: String by value("D:\\Users\\CSSXSH\\Vits\\MoeGoe\\MoeGoe.exe")

    @ConsoleExperimentalApi
    override fun onInit(owner: PluginDataHolder, storage: PluginDataStorage) {
        super.onInit(owner, storage)

        System.setProperty("xyz.cssxsh.mirai.vits.moegoe", moegoe)
    }
}