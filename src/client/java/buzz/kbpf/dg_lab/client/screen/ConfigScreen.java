package buzz.kbpf.dg_lab.client.screen;

import buzz.kbpf.dg_lab.client.Dg_labClient;
import buzz.kbpf.dg_lab.client.createQR.ToolQR;
import buzz.kbpf.dg_lab.client.entity.ModConfig;
import buzz.kbpf.dg_lab.client.entity.StrengthConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;

@Environment(EnvType.CLIENT)
public class ConfigScreen extends Screen {
    public ConfigScreen() {
        // 此参数为屏幕的标题，进入屏幕中，复述功能会复述。
        super(Text.literal("我的教程屏幕"));
    }


    public ButtonWidget button1;
    public ButtonWidget button2;
    public ButtonWidget createQR;

    public SliderWidget ADamageStrength;
    public SliderWidget BDamageStrength;
    public SliderWidget ADelayTime;
    public SliderWidget BDelayTime;
    public SliderWidget ADownTime;
    public SliderWidget BDownTime;
    public SliderWidget ADownValue;
    public SliderWidget BDownValue;
    public SliderWidget ADeathStrength;
    public SliderWidget BDeathStrength;
    public SliderWidget ADeathDelay;
    public SliderWidget BDeathDelay;
    public SliderWidget RenderingPositionX;
    public SliderWidget RenderingPositionY;


    @Override
    protected void init() {

        StrengthConfig strengthConfig = Dg_labClient.getStrengthConfig();
        ModConfig modConfig = Dg_labClient.getModConfig();
        MinecraftClient client = MinecraftClient.getInstance();
        ADamageStrength = new SliderWidget(width / 2 - 205, 50, 100, 20, Text.literal("A每伤害强度" + String.format("%.2f", strengthConfig.getADamageStrength())),  strengthConfig.getADamageStrength() / 20) {
            @Override
            protected void updateMessage() {}

            @Override
            protected void applyValue() {
                float ADamageStrength = (float) (this.value * 20);
                strengthConfig.setADamageStrength(ADamageStrength);
                this.setMessage(Text.literal("A每伤害强度" + String.format("%.2f", strengthConfig.getADamageStrength())));
            }
        };

        BDamageStrength = new SliderWidget(width / 2 - 105, 50, 100, 20, Text.literal("B每伤害强度" + String.format("%.2f", strengthConfig.getBDamageStrength())),  strengthConfig.getBDamageStrength() / 20) {
            @Override
            protected void updateMessage() {}

            @Override
            protected void applyValue() {
                float BDamageStrength = (float) (this.value * 20);
                strengthConfig.setBDamageStrength(BDamageStrength);
                this.setMessage(Text.literal("B每伤害强度" + String.format("%.2f", strengthConfig.getBDamageStrength())));
            }
        };

        ADelayTime = new SliderWidget(width / 2 + 5, 50, 100, 20, Text.literal("A强度下降等待" + strengthConfig.getADelayTime() * 40 + "ms"),  (double) strengthConfig.getADelayTime() / 125) {
            @Override
            protected void updateMessage() {}

            @Override
            protected void applyValue() {
                int tmp = (int) (this.value * 125);
                this.setMessage(Text.literal("A强度下降等待" + tmp * 40 + "ms"));
                strengthConfig.setADelayTime(tmp);
            }
        };

        BDelayTime = new SliderWidget(width / 2 + 105, 50, 100, 20, Text.literal("B强度下降等待" + strengthConfig.getBDelayTime() * 40 + "ms"),  (double) strengthConfig.getBDelayTime() / 125) {
            @Override
            protected void updateMessage() {}

            @Override
            protected void applyValue() {
                int tmp = (int) (this.value * 125);
                this.setMessage(Text.literal("B强度下降等待" + tmp * 40 + "ms"));
                strengthConfig.setBDelayTime(tmp);
            }
        };

        ADownTime = new SliderWidget(width / 2 + 5, 80, 100, 20, Text.literal("A强度下降间隔" + strengthConfig.getADownTime() * 40 + "ms"),  (double) strengthConfig.getADownTime() / 125) {
            @Override
            protected void updateMessage() {}

            @Override
            protected void applyValue() {
                int tmp = (int) (this.value * 125);
                if(tmp == 0) tmp = 1;
                this.setMessage(Text.literal("A强度下降间隔" + tmp * 40 + "ms"));
                strengthConfig.setADownTime(tmp);
            }
        };

        BDownTime = new SliderWidget(width / 2 + 105, 80, 100, 20, Text.literal("B强度下降间隔" + strengthConfig.getBDownTime() * 40 + "ms"),  (double) strengthConfig.getBDownTime() / 125) {
            @Override
            protected void updateMessage() {}

            @Override
            protected void applyValue() {
                int tmp = (int) (this.value * 125);
                if(tmp == 0) tmp = 1;
                this.setMessage(Text.literal("B强度下降间隔" + tmp * 40 + "ms"));
                strengthConfig.setBDownTime(tmp);
            }
        };

        ADownValue = new SliderWidget(width / 2 - 205, 80, 100, 20, Text.literal("A强度下降数值" + strengthConfig.getADownValue()),  (double) strengthConfig.getADownValue() / 20) {
            @Override
            protected void updateMessage() {}

            @Override
            protected void applyValue() {
                int tmp = (int) (this.value * 20);
                this.setMessage(Text.literal("A强度下降数值" + tmp));
                strengthConfig.setADownValue(tmp);
            }
        };

        BDownValue = new SliderWidget(width / 2 - 105, 80, 100, 20, Text.literal("A强度下降数值" + strengthConfig.getBDownValue()),  (double) strengthConfig.getBDownValue() / 20) {
            @Override
            protected void updateMessage() {}

            @Override
            protected void applyValue() {
                int tmp = (int) (this.value * 20);
                this.setMessage(Text.literal("B强度下降数值" + tmp));
                strengthConfig.setBDownValue(tmp);
            }
        };

        ADeathStrength = new SliderWidget(width / 2 - 205, 110, 100, 20, Text.literal("A死亡增加强度" + strengthConfig.getADeathStrength()),  (double) strengthConfig.getADeathStrength() / 70) {
            @Override
            protected void updateMessage() {}

            @Override
            protected void applyValue() {
                int tmp = (int) (this.value * 70);
                strengthConfig.setADeathStrength(tmp);
                this.setMessage(Text.literal("A死亡增加强度" + tmp));
            }
        };

        BDeathStrength = new SliderWidget(width / 2 - 105, 110, 100, 20, Text.literal("B死亡增加强度" + strengthConfig.getBDeathStrength()),  (double) strengthConfig.getBDeathStrength() / 70) {
            @Override
            protected void updateMessage() {}

            @Override
            protected void applyValue() {
                int tmp = (int) (this.value * 70);
                strengthConfig.setBDeathStrength(tmp);
                this.setMessage(Text.literal("B死亡增加强度" + tmp));
            }
        };

        ADeathDelay = new SliderWidget(width / 2 + 5, 110, 100, 20, Text.literal("A死亡时强度下降等待" + strengthConfig.getADeathDelay() * 40 + "ms"),  (double) strengthConfig.getADeathDelay() / 125) {
            @Override
            protected void updateMessage() {}

            @Override
            protected void applyValue() {
                int tmp = (int) (this.value * 125);
                this.setMessage(Text.literal("A下降等待" + tmp * 40 + "ms"));
                strengthConfig.setADeathDelay(tmp);
            }
        };

        BDeathDelay = new SliderWidget(width / 2 + 105, 110, 100, 20, Text.literal("B死亡时强度下降等待" + strengthConfig.getBDeathDelay() * 40 + "ms"),  (double) strengthConfig.getBDeathDelay() / 125) {
            @Override
            protected void updateMessage() {}

            @Override
            protected void applyValue() {
                int tmp = (int) (this.value * 125);
                this.setMessage(Text.literal("B下降等待" + tmp * 40 + "ms"));
                strengthConfig.setBDeathDelay(tmp);
            }
        };


        int width1 = client.getWindow().getScaledWidth(), height1 = client.getWindow().getScaledHeight();

        RenderingPositionX = new SliderWidget(width / 2 + 5, 140, 100, 20, Text.literal("强度显示位置X:" + modConfig.getRenderingPositionX()),  (double) modConfig.getRenderingPositionX() / width1) {
            @Override
            protected void updateMessage() {}

            @Override
            protected void applyValue() {
                int tmp = (int) (this.value * width1);
                modConfig.setRenderingPositionX(tmp);
                if(modConfig.getRenderingPositionX() >= width1 || modConfig.getRenderingPositionY() >= height1) this.setMessage(Text.literal("已关闭强度显示"));
                else this.setMessage(Text.literal("强度显示位置X:" + tmp));
            }
        };

        RenderingPositionY = new SliderWidget(width / 2 + 105, 140, 100, 20, Text.literal("强度显示位置Y:" + modConfig.getRenderingPositionY()),  (double) modConfig.getRenderingPositionY() / height1) {
            @Override
            protected void updateMessage() {}

            @Override
            protected void applyValue() {
                int tmp = (int) (this.value * height1);
                modConfig.setRenderingPositionY(tmp);
                if(modConfig.getRenderingPositionX() >= width1 || modConfig.getRenderingPositionY() >= height1) this.setMessage(Text.literal("已关闭强度显示"));
                else this.setMessage(Text.literal("强度显示位置Y:" + tmp));
            }
        };
        
        button1 = ButtonWidget.builder(Text.literal("保存配置到文件"), button -> {
                    strengthConfig.savaFile();
                    modConfig.savaFile();
                })
                .dimensions(width / 2 - 205, 20, 200, 20).tooltip(Tooltip.of(Text.literal("所有更改是临时更改\n点击此按钮保存到文件"))).build();
        button2 = ButtonWidget.builder(Text.literal("连接服务器自动启动已" + (modConfig.getAutoStartWebSocketServer() ? "开启" : "关闭")), button -> {
                    if(modConfig.getAutoStartWebSocketServer()) {
                        button.setMessage(Text.literal("连接服务器自动启动已关闭"));
                        modConfig.setAutoStartWebSocketServer(false);
                    }else {
                        button.setMessage(Text.literal("连接服务器自动启动已开启"));
                        modConfig.setAutoStartWebSocketServer(true);
                    }
                })
                .dimensions(width / 2 + 5, 20, 200, 20).tooltip(Tooltip.of(Text.literal("要在客户端启动时自动启动连接服务器\n如果关闭需要使用指令手动启动"))).build();

        createQR = ButtonWidget.builder(Text.literal("创建连接二维码并打开"), button -> {
            ToolQR.CreateQR();
        }).dimensions(width / 2 - 205, 140, 200, 20).tooltip(Tooltip.of(Text.literal("图片默认生成于此地址:\n" + System.getProperty("user.dir")))).build();


        addDrawableChild(button1);
        addDrawableChild(button2);
        addDrawableChild(ADamageStrength);
        addDrawableChild(BDamageStrength);
        addDrawableChild(ADelayTime);
        addDrawableChild(BDelayTime);
        addDrawableChild(ADownTime);
        addDrawableChild(BDownTime);
        addDrawableChild(ADownValue);
        addDrawableChild(BDownValue);
        addDrawableChild(ADeathStrength);
        addDrawableChild(BDeathStrength);
        addDrawableChild(ADeathDelay);
        addDrawableChild(BDeathDelay);
        addDrawableChild(createQR);
        addDrawableChild(RenderingPositionX);
        addDrawableChild(RenderingPositionY);
    }
}