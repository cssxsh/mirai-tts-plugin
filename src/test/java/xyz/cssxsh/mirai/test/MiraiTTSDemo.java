package xyz.cssxsh.mirai.test;

import kotlin.Unit;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;
import xyz.cssxsh.baidu.aip.tts.SpeechOption;
import xyz.cssxsh.baidu.aip.tts.SpeechPerson;
import xyz.cssxsh.mirai.tts.MiraiTextToSpeech;

public class MiraiTTSDemo extends JavaPlugin {
    public MiraiTTSDemo() {
        super(new JvmPluginDescriptionBuilder("xyz.cssxsh.mirai.plugin.mirai-hibernate-demo", "0.0.0")
                .dependsOn("xyz.cssxsh.mirai.plugin.mirai-tts-plugin", false)
                .build());
    }


    @Override
    public void onEnable() {
        byte[] bytes0 = MiraiTextToSpeech.INSTANCE.speech("测试");

        SpeechOption option = new SpeechOption();
        option.setPerson(SpeechPerson.Top.MatureFemale);
        byte[] bytes1 = MiraiTextToSpeech.INSTANCE.speech("测试", option);

        byte[] bytes2 = MiraiTextToSpeech.INSTANCE.speech("测试", (o) -> {
            o.setPerson(SpeechPerson.Top.MatureFemale);
            return Unit.INSTANCE;
        });
    }
}
