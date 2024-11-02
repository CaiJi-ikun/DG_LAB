package online.kbpf.dg_lab.client.screen.WaveformScreen;

import online.kbpf.dg_lab.client.screen.ConfigScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class WaveformConfigScreen extends Screen {


    public WaveformListWidget waveformListWidget;

    public WaveformConfigScreen() {

        super(Text.literal("波形配置界面"));
    }

    @Override
    public void close() {
        Screen configScreen = new ConfigScreen();
        client.setScreen(configScreen);
    }

    @Override
    protected void init() {
        MinecraftClient client = MinecraftClient.getInstance();
        System.out.println(width + " " + height);
        waveformListWidget = new WaveformListWidget(client, width, height - 40, 40, 20);
        WaveformListWidget.Entry a = new WaveformListWidget.Entry(client.textRenderer, Text.literal("a"), () -> {
            System.out.println("a");
        });
        WaveformListWidget.Entry b = new WaveformListWidget.Entry(client.textRenderer, Text.literal("b"), () -> {
            System.out.println("b");
        });
        WaveformListWidget.Entry c = new WaveformListWidget.Entry(client.textRenderer, Text.literal("c"), () -> {
            System.out.println("c");
        });
        WaveformListWidget.Entry d = new WaveformListWidget.Entry(client.textRenderer, Text.literal("d"), () -> {
            System.out.println("d");
        });
        WaveformListWidget.Entry e = new WaveformListWidget.Entry(client.textRenderer, Text.literal("e"), () -> {
            System.out.println("e");
        });
        WaveformListWidget.Entry f = new WaveformListWidget.Entry(client.textRenderer, Text.literal("f"), () -> {
            System.out.println("f");
        });
        WaveformListWidget.Entry g = new WaveformListWidget.Entry(client.textRenderer, Text.literal("g"), () -> {
            System.out.println("g");
        });
        WaveformListWidget.Entry h = new WaveformListWidget.Entry(client.textRenderer, Text.literal("h"), () -> {
            System.out.println("h");
        });


        waveformListWidget.addWaveformEntry(a);
        waveformListWidget.addWaveformEntry(b);
        waveformListWidget.addWaveformEntry(c);
        waveformListWidget.addWaveformEntry(d);
        waveformListWidget.addWaveformEntry(e);
        waveformListWidget.addWaveformEntry(f);
        waveformListWidget.addWaveformEntry(g);
        waveformListWidget.addWaveformEntry(h);
        addDrawableChild(waveformListWidget);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
    }


//    @Override
//    public boolean mouseClicked(double mouseX, double mouseY, int button) {
//        if (super.mouseClicked(mouseX, mouseY, button)) return true;
//        return waveformListWidget.mouseClicked(mouseX, mouseY, button);
//    }
}
