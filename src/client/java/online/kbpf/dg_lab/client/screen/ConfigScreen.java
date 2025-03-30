package online.kbpf.dg_lab.client.screen;


import online.kbpf.dg_lab.client.createQR.ToolQR;
import online.kbpf.dg_lab.client.Config.WaveformConfig;
import online.kbpf.dg_lab.client.screen.StrengthScreen.StrengthConfigScreen;
import online.kbpf.dg_lab.client.screen.WaveformScreen.WaveformConfigScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;


import static online.kbpf.dg_lab.client.Dg_labClient.waveformMap;
import static online.kbpf.dg_lab.client.Dg_labClient.strengthConfig;
import static online.kbpf.dg_lab.client.Dg_labClient.modConfig;


@Environment(EnvType.CLIENT)
public class ConfigScreen extends Screen {

    public static final int ButtonHeight = 20, ButtonDistance = 5;

    public ButtonWidget saveFile;
    public ButtonWidget webSocketConfig;
    public ButtonWidget createQR;
    public ButtonWidget StrengthConfig;
    public ButtonWidget WaveFormConfig;
    public ButtonWidget CustomConfig;
    public ButtonWidget MaxStrength;


    public SliderWidget RenderingPositionX;
    public SliderWidget RenderingPositionY;






//    Screen customScreen = new CustomScreen();

    public ConfigScreen() {
        // 此参数为屏幕的标题，进入屏幕中，复述功能会复述。
        super(Text.of("配置界面"));
    }





    @Override
    protected void init() {


//        online.kbpf.dg_lab.client.Config.StrengthConfig StrengthConfig = Dg_labClient.getStrengthConfig();
//        ModConfig modConfig = Dg_labClient.getModConfig();
        MinecraftClient client = MinecraftClient.getInstance();


        int width1 = client.getWindow().getScaledWidth(), height1 = client.getWindow().getScaledHeight();

        CustomConfig = ButtonWidget.builder(Text.of("test"), button -> {
//            client.setScreen(customScreen);
        }).dimensions((int) ((double) width / 2 - (width * 0.4) - 5), 140, (int) (width * 0.4), ButtonHeight).build();






        int buttonX = width / 2 + 5;

        RenderingPositionX = new SliderWidget(buttonX, 140 - ButtonDistance - ButtonHeight, (int) (width * 0.2) - 6, ButtonHeight, Text.of((modConfig.getRenderingPositionX() >= width1 || modConfig.getRenderingPositionY() >= height1) ? "已关闭强度显示" : ("显示位置X:" + modConfig.getRenderingPositionX())), (double) modConfig.getRenderingPositionX() / width1) {
            @Override
            protected void updateMessage() {
            }

            @Override
            protected void applyValue() {
                int tmp = (int) (this.value * width1);
                modConfig.setRenderingPositionX(tmp);
                if (modConfig.getRenderingPositionX() >= width1 || modConfig.getRenderingPositionY() >= height1) {
                    this.setMessage(Text.of("已关闭强度显示"));
                    RenderingPositionY.setMessage(Text.of("已关闭强度显示"));
                } else {
                    this.setMessage(Text.of("显示位置X:" + tmp));
                    RenderingPositionY.setMessage(Text.of("显示位置Y:" + modConfig.getRenderingPositionY()));
                }
            }
        };



        RenderingPositionY = new SliderWidget(buttonX + RenderingPositionX.getWidth(), 140 - ButtonDistance - ButtonHeight, (int) (width * 0.2) - 6, ButtonHeight, Text.of((modConfig.getRenderingPositionX() >= width1 || modConfig.getRenderingPositionY() >= height1) ? "已关闭强度显示" : ("显示位置Y:" + modConfig.getRenderingPositionY())), (double) modConfig.getRenderingPositionY() / height1) {
            @Override
            protected void updateMessage() {
            }

            @Override
            protected void applyValue() {
                int tmp = (int) (this.value * height1);
                modConfig.setRenderingPositionY(tmp);
                if (modConfig.getRenderingPositionX() >= width1 || modConfig.getRenderingPositionY() >= height1) {
                    this.setMessage(Text.of("已关闭强度显示"));
                    RenderingPositionX.setMessage(Text.of("已关闭强度显示"));
                } else {
                    this.setMessage(Text.of("显示位置Y:" + tmp));
                    RenderingPositionX.setMessage(Text.of("显示位置X:" + modConfig.getRenderingPositionX()));
                }
            }
        };

        MaxStrength = ButtonWidget.builder(Text.of((modConfig.isRenderingMax()) ? "开" : "关"), button -> {
            modConfig.setRenderingMax(!modConfig.isRenderingMax());
            MaxStrength.setMessage(Text.of((modConfig.isRenderingMax()) ? "开" : "关"));
        }).dimensions(RenderingPositionY.getX() + RenderingPositionX.getWidth(), 140 - ButtonDistance - ButtonHeight, 12, ButtonHeight).tooltip(Text.of("是否开启最大强度显示")).build();

//        MaxStrength = ButtonWidget()


        saveFile = ButtonWidget.builder(Text.of("保存配置到文件"), button -> {
                    strengthConfig.savaFile();
                    modConfig.savaFile();
                    WaveformConfig.saveWaveform(waveformMap);
                })
                .dimensions((int) ((double) width / 2 - (width * 0.4) - 5), 20, (int) (width * 0.4), ButtonHeight).tooltip(Text.of("所有更改是临时更改\n点击此按钮保存到文件")).build();



        webSocketConfig = ButtonWidget.builder(Text.of("连接设置"), button -> {
                    Screen WebSocketConfigScreen = new WebSocketConfigScreen();
                    client.setScreen(WebSocketConfigScreen);
                })
                .dimensions(width / 2 + 5, 20, (int) (width * 0.4), ButtonHeight).tooltip(Text.of("点击修改连接设置\n非必要无需修改")).build();

        StrengthConfig = ButtonWidget.builder(Text.of("强度设置"), button -> {
            Screen strengthConfigScreen = new StrengthConfigScreen();
            client.setScreen(strengthConfigScreen);
        }).dimensions((int) ((double) width / 2 - (width * 0.4) - 5), 20 + ButtonHeight + ButtonDistance, (int) (width * 0.4), ButtonHeight).tooltip(Text.of("点击修改强度设置")).build();

        WaveFormConfig = ButtonWidget.builder(Text.of("波形设置"), button -> {
            Screen waveformConfigScreen = new WaveformConfigScreen();
            client.setScreen(waveformConfigScreen);
        }).dimensions(width / 2 + 5, 20 + ButtonHeight + ButtonDistance, (int) (width * 0.4), ButtonHeight).tooltip((Text.of(":P"))).build();

        createQR = ButtonWidget.builder(Text.of("创建连接二维码并打开"), button -> {
            ToolQR.CreateQR();
        }).dimensions((int) ((double) width / 2 - (width * 0.4) - 5), 140 - ButtonDistance - ButtonHeight, (int) (width * 0.4), ButtonHeight).tooltip(Text.of("图片默认生成于此地址:\n" + System.getProperty("user.dir"))).build();




        addDrawableChild(saveFile);
        addDrawableChild(webSocketConfig);
        addDrawableChild(StrengthConfig);
        addDrawableChild(WaveFormConfig);
        addDrawableChild(createQR);
        addDrawableChild(RenderingPositionX);
        addDrawableChild(RenderingPositionY);
        addDrawableChild(MaxStrength);
//        addDrawableChild(CustomConfig);
    }




}