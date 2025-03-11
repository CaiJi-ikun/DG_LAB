package online.kbpf.dg_lab.client.screen.WaveformScreen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import online.kbpf.dg_lab.client.screen.ConfigScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

@Environment(EnvType.CLIENT)
public class WaveformConfigScreen extends Screen {

//波形配置界面
    private WaveformListWidget waveformListWidget;

    public WaveformConfigScreen() {

        super(Text.literal("波形配置界面"));
    }

    @Override
    public void close() {
        Screen configScreen = new ConfigScreen();
        if (client != null) {
            client.setScreen(configScreen);
        }
        //上一级界面
    }

    @Override
    protected void init() {
        //注册列表项目
        MinecraftClient client = MinecraftClient.getInstance();
        waveformListWidget = new WaveformListWidget(client, width, height - 40, 40, 25);
        //用这个滚动列表注意左右边界 添加条目比较少的时候不显示左右边界 但是左右边界的地方无法交互
        WaveformListWidget.Entry a = new WaveformListWidget.Entry(client.textRenderer, Text.literal("A通道受伤波形"), "ADamage");
        WaveformListWidget.Entry b = new WaveformListWidget.Entry(client.textRenderer, Text.literal("A通道恢复波形"), "AHealing");
        WaveformListWidget.Entry c = new WaveformListWidget.Entry(client.textRenderer, Text.literal("B通道受伤波形"), "BDamage");
        WaveformListWidget.Entry d = new WaveformListWidget.Entry(client.textRenderer, Text.literal("B通道恢复波形"), "BHealing");


        //添加列表项目
        waveformListWidget.addWaveformEntry(a);
        waveformListWidget.addWaveformEntry(b);
        waveformListWidget.addWaveformEntry(c);
        waveformListWidget.addWaveformEntry(d);
        addDrawableChild(waveformListWidget);
    }

}

//    @Override
//    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
//        //渲染
//        super.render(context, mouseX, mouseY, delta);
//    }


//    @Override
//    public boolean mouseClicked(double mouseX, double mouseY, int button) {
//        if (super.mouseClicked(mouseX, mouseY, button)) return true;
//        return waveformListWidget.mouseClicked(mouseX, mouseY, button);
//    }

