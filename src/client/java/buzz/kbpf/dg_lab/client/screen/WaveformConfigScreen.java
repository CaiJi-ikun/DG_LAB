package buzz.kbpf.dg_lab.client.screen;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class WaveformConfigScreen extends Screen {

    protected WaveformConfigScreen() {
        super(Text.literal("波形配置界面"));
    }
    @Override
    public void close(){
        Screen configScreen = new ConfigScreen();
        client.setScreen(configScreen);
    }

    @Override
    protected void init(){

    }

}
