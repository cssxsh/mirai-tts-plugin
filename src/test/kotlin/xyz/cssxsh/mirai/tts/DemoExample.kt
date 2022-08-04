package xyz.cssxsh.mirai.tts

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DemoExample(
    @SerialName("defaultText")
    val defaultText: String,
    @SerialName("name")
    val name: String,
    @SerialName("person")
    val person: Int,
    @SerialName("profile")
    val profile: String
)