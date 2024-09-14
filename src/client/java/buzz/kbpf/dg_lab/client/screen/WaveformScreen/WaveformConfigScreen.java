package buzz.kbpf.dg_lab.client.screen.WaveformScreen;

import buzz.kbpf.dg_lab.client.screen.ConfigScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class WaveformConfigScreen extends Screen {



    public WaveformConfigScreen() {

        super(Text.literal("波形配置界面"));
    }
    @Override
    public void close(){
        Screen configScreen = new ConfigScreen();
        client.setScreen(configScreen);
    }

    public WaveformListWidget waveformListWidget;


    @Override
    protected void init(){
        MinecraftClient client = MinecraftClient.getInstance();
        waveformListWidget = new WaveformListWidget(client, width, height - 40, 40, 30);
        WaveformListWidget.Entry a = new WaveformListWidget.Entry(client.textRenderer, Text.literal("abc"), () -> {
            System.out.println("abc1");
        });
        WaveformListWidget.Entry b = new WaveformListWidget.Entry(client.textRenderer, Text.literal("123"), () -> {
            System.out.println("abc2");
        });


        waveformListWidget.addWaveformEntry(a);
        waveformListWidget.addWaveformEntry(b);
        addDrawableChild(waveformListWidget);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (super.mouseClicked(mouseX, mouseY, button)) return true;
        return waveformListWidget.mouseClicked(mouseX, mouseY, button);
    }
}
