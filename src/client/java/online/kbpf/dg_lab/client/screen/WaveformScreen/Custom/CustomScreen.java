package online.kbpf.dg_lab.client.screen.WaveformScreen.Custom;



import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import online.kbpf.dg_lab.client.entity.Waveform.ControlBar;
import online.kbpf.dg_lab.client.entity.Waveform.Waveform;
import online.kbpf.dg_lab.client.screen.WaveformScreen.WaveformConfigScreen;

import static online.kbpf.dg_lab.client.Dg_labClient.waveformMap;

import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
public class CustomScreen extends Screen {



    private ButtonWidget add, delete;
    private CustomListWidget customListWidget;
    protected static List<ControlBar> list = new ArrayList<>();
    protected String waveformKey;

    public CustomScreen(String waveformKey) {

        super(Text.literal("自定义波形界面"));
        if(waveformMap.containsKey(waveformKey)) {
            list = waveformMap.get(waveformKey).getList();
        }
        this.waveformKey = waveformKey;
    }


    @Override
    public void close() {
        Screen backScreen = new WaveformConfigScreen();
        Waveform tmp = new Waveform();
        if(waveformMap.containsKey(this.waveformKey))
            waveformMap.get(this.waveformKey);


        tmp.setList(list);
        tmp.GraphToData();
        if (waveformMap.containsKey(this.waveformKey))
            waveformMap.put(this.waveformKey, tmp);
        else
            waveformMap.replace(this.waveformKey, tmp);

        client.setScreen(backScreen);
    }

    @Override
    protected void init() {
        MinecraftClient client = MinecraftClient.getInstance();
        customListWidget = new CustomListWidget(client, width, height - 40, 20, 25);


        add = ButtonWidget.builder(Text.literal("+"), button -> {
            for(int i = 1; i <= 4; i++) {
                list.add(new ControlBar());
                customListWidget.addCustomEntry(new CustomListWidget.Entry(list.size() - 1));
            }

        }).dimensions((int) (width * 0.1), height - 17, (int) (width * 0.7), 15).build();

        delete = ButtonWidget.builder(Text.literal("-"), button -> {
            if(list.size() > 7) {
                for (int i = 1; i <= 4; i++) {
                    customListWidget.removeLast();
                    list.removeLast();
                }
            }

        }).dimensions((int) (width * 0.8), height - 17, (int) (width * 0.1), 15).build();

        for (int i = 0; i <list.size(); i++){
            customListWidget.addCustomEntry(new CustomListWidget.Entry(i));
        }




        addDrawableChild(add);
        addDrawableChild(delete);
        addDrawableChild(customListWidget);

    }


}

