package online.kbpf.dg_lab.client.screen;

import online.kbpf.dg_lab.client.Dg_labClient;
import online.kbpf.dg_lab.client.createQR.ToolQR;
import online.kbpf.dg_lab.client.entity.ModConfig;
import online.kbpf.dg_lab.client.entity.WaveformConfig;
import online.kbpf.dg_lab.client.screen.StrengthScreen.StrengthConfigScreen;
import online.kbpf.dg_lab.client.screen.WaveformScreen.WaveformConfigScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;

import static online.kbpf.dg_lab.client.Dg_labClient.waveformDataMap;

@Environment(EnvType.CLIENT)
public class ConfigScreen extends Screen {

    public ButtonWidget saveFile;
    public ButtonWidget webSocketConfig;
    public ButtonWidget createQR;
    public ButtonWidget StrengthConfig;
    public ButtonWidget WaveFormConfig;


    public SliderWidget RenderingPositionX;
    public SliderWidget RenderingPositionY;



    public ConfigScreen() {
        // 此参数为屏幕的标题，进入屏幕中，复述功能会复述。
        super(Text.literal("配置界面"));
    }





    @Override
    protected void init() {
        Screen WebSocketConfigScreen = new WebSocketConfigScreen();
        Screen strengthConfigScreen = new StrengthConfigScreen();
        Screen waveformConfigScreen = new WaveformConfigScreen();

        online.kbpf.dg_lab.client.entity.StrengthConfig strengthConfig = Dg_labClient.getStrengthConfig();
        ModConfig modConfig = Dg_labClient.getModConfig();
        MinecraftClient client = MinecraftClient.getInstance();


        int width1 = client.getWindow().getScaledWidth(), height1 = client.getWindow().getScaledHeight();



        RenderingPositionX = new SliderWidget(width / 2 + 5, 120, (int) (width * 0.2), 15, Text.literal((modConfig.getRenderingPositionX() >= width1 || modConfig.getRenderingPositionY() >= height1) ? "已关闭强度显示" : ("强度显示位置X:" + modConfig.getRenderingPositionX())), (double) modConfig.getRenderingPositionX() / width1) {
            @Override
            protected void updateMessage() {
            }

            @Override
            protected void applyValue() {
                int tmp = (int) (this.value * width1);
                modConfig.setRenderingPositionX(tmp);
                if (modConfig.getRenderingPositionX() >= width1 || modConfig.getRenderingPositionY() >= height1) {
                    this.setMessage(Text.literal("已关闭强度显示"));
                    RenderingPositionY.setMessage(Text.literal("已关闭强度显示"));
                } else {
                    this.setMessage(Text.literal("强度显示位置X:" + tmp));
                    RenderingPositionY.setMessage(Text.literal("强度显示位置Y:" + modConfig.getRenderingPositionY()));
                }
            }
        };

        RenderingPositionY = new SliderWidget((int) ((double) width / 2 + (width * 0.2) + 5), 120, (int) (width * 0.2), 15, Text.literal((modConfig.getRenderingPositionX() >= width1 || modConfig.getRenderingPositionY() >= height1) ? "已关闭强度显示" : ("强度显示位置Y:" + modConfig.getRenderingPositionY())), (double) modConfig.getRenderingPositionY() / height1) {
            @Override
            protected void updateMessage() {
            }

            @Override
            protected void applyValue() {
                int tmp = (int) (this.value * height1);
                modConfig.setRenderingPositionY(tmp);
                if (modConfig.getRenderingPositionX() >= width1 || modConfig.getRenderingPositionY() >= height1) {
                    this.setMessage(Text.literal("已关闭强度显示"));
                    RenderingPositionX.setMessage(Text.literal("已关闭强度显示"));
                } else {
                    this.setMessage(Text.literal("强度显示位置Y:" + tmp));
                    RenderingPositionX.setMessage(Text.literal("强度显示位置X:" + modConfig.getRenderingPositionX()));
                }
            }
        };

        saveFile = ButtonWidget.builder(Text.literal("保存配置到文件"), button -> {
                    strengthConfig.savaFile();
                    modConfig.savaFile();
                    WaveformConfig.saveWaveform(waveformDataMap);
                })
                .dimensions((int) ((double) width / 2 - (width * 0.4) - 5), 20, (int) (width * 0.4), 15).tooltip(Tooltip.of(Text.literal("所有更改是临时更改\n点击此按钮保存到文件"))).build();



        webSocketConfig = ButtonWidget.builder(Text.literal("连接设置"), button -> {
                    client.setScreen(WebSocketConfigScreen);
                })
                .dimensions(width / 2 + 5, 20, (int) (width * 0.4), 15).tooltip(Tooltip.of(Text.literal("点击修改连接设置\n非必要无需修改"))).build();

        StrengthConfig = ButtonWidget.builder(Text.literal("强度设置"), button -> {
            client.setScreen(strengthConfigScreen);
        }).dimensions((int) ((double) width / 2 - (width * 0.4) - 5), 45, (int) (width * 0.4), 15).tooltip(Tooltip.of(Text.literal("点击修改强度设置"))).build();

        WaveFormConfig = ButtonWidget.builder(Text.literal("波形设置"), button -> {
            client.setScreen(waveformConfigScreen);
        }).dimensions(width / 2 + 5, 45, (int) (width * 0.4), 15).tooltip((Tooltip.of(Text.literal("制作中")))).build();

        createQR = ButtonWidget.builder(Text.literal("创建连接二维码并打开"), button -> {
            ToolQR.CreateQR();
        }).dimensions((int) ((double) width / 2 - (width * 0.4) - 5), 120, (int) (width * 0.4), 15).tooltip(Tooltip.of(Text.literal("图片默认生成于此地址:\n" + System.getProperty("user.dir")))).build();




        addDrawableChild(saveFile);
        addDrawableChild(webSocketConfig);
        addDrawableChild(StrengthConfig);
        addDrawableChild(WaveFormConfig);
        addDrawableChild(createQR);
        addDrawableChild(RenderingPositionX);
        addDrawableChild(RenderingPositionY);
    }

    public void test(String text) {
        System.out.println(text);
    }


}