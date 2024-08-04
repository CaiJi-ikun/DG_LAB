package buzz.kbpf.dg_lab.client.screen;

import buzz.kbpf.dg_lab.client.Dg_labClient;
import buzz.kbpf.dg_lab.client.createQR.ToolQR;
import buzz.kbpf.dg_lab.client.entity.ModConfig;
import buzz.kbpf.dg_lab.client.entity.StrengthConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

@Environment(EnvType.CLIENT)
public class ConfigScreen extends Screen {

    public ButtonWidget button1;
    public ButtonWidget button2;
    public ButtonWidget createQR;

    public SliderWidget ADamageStrength;
    public SliderWidget BDamageStrength;
    public ButtonWidget DamageStrength;
    public SliderWidget ADelayTime;
    public SliderWidget BDelayTime;
    public ButtonWidget DelayTime;
    public SliderWidget ADownTime;
    public SliderWidget BDownTime;
    public ButtonWidget DownTime;
    public SliderWidget ADownValue;
    public SliderWidget BDownValue;
    public ButtonWidget DownValue;
    public SliderWidget ADeathStrength;
    public SliderWidget BDeathStrength;
    public ButtonWidget DeathStrength;
    public SliderWidget ADeathDelay;
    public SliderWidget BDeathDelay;
    public ButtonWidget DeathDelay;
    public SliderWidget RenderingPositionX;
    public SliderWidget RenderingPositionY;
//    public TextFieldWidget textFieldWidget;


    public ConfigScreen() {
        // 此参数为屏幕的标题，进入屏幕中，复述功能会复述。
        super(Text.literal("配置界面"));
    }


//    public String getInputText(){
//        return this.textFieldWidget.getText();
//    }



    @Override
    protected void init() {
        Screen WebSocketConfigScreen = new WebSocketConfigScreen();

        StrengthConfig strengthConfig = Dg_labClient.getStrengthConfig();
        ModConfig modConfig = Dg_labClient.getModConfig();
        MinecraftClient client = MinecraftClient.getInstance();
        ADamageStrength = new SliderWidget(width / 2 - 205, 45, 100, 15, Text.literal("A每伤害强度" + String.format("%.2f", strengthConfig.getADamageStrength())),  strengthConfig.getADamageStrength() / 20) {
            @Override
            protected void updateMessage() {}

            @Override
            protected void applyValue() {
                float ADamageStrength = (float) (this.value * 20);
                strengthConfig.setADamageStrength(ADamageStrength);
                this.setMessage(Text.literal("A每伤害强度" + String.format("%.2f", strengthConfig.getADamageStrength())));
            }
        };

        BDamageStrength = new SliderWidget(width / 2 - 105, 45, 100, 15, Text.literal("B每伤害强度" + String.format("%.2f", strengthConfig.getBDamageStrength())),  strengthConfig.getBDamageStrength() / 20) {
            @Override
            protected void updateMessage() {}

            @Override
            protected void applyValue() {
                float BDamageStrength = (float) (this.value * 20);
                strengthConfig.setBDamageStrength(BDamageStrength);
                this.setMessage(Text.literal("B每伤害强度" + String.format("%.2f", strengthConfig.getBDamageStrength())));
            }
        };

        DamageStrength = ButtonWidget.builder(Text.literal("?"), button -> {}).dimensions(width / 2 - 215, 45, 10, 15).tooltip(Tooltip.of(Text.literal("每受到半颗心伤害增加的强度\n受伤时增加强度若小于1则增加1\n大于一的强度数值9舍0入\n若为0则不增加"))).build();

        ADelayTime = new SliderWidget(width / 2 + 5, 45, 100, 15, Text.literal("A强度下降等待" + strengthConfig.getADelayTime() * 50 + "ms"),  (double) strengthConfig.getADelayTime() / 120) {
            @Override
            protected void updateMessage() {}

            @Override
            protected void applyValue() {
                int tmp = (int) (this.value * 120);
                this.setMessage(Text.literal("A强度下降等待" + tmp * 50 + "ms"));
                strengthConfig.setADelayTime(tmp);
            }
        };

        BDelayTime = new SliderWidget(width / 2 + 105, 45, 100, 15, Text.literal("B强度下降等待" + strengthConfig.getBDelayTime() * 50 + "ms"),  (double) strengthConfig.getBDelayTime() / 120) {
            @Override
            protected void updateMessage() {}

            @Override
            protected void applyValue() {
                int tmp = (int) (this.value * 120);
                this.setMessage(Text.literal("B强度下降等待" + tmp * 50 + "ms"));
                strengthConfig.setBDelayTime(tmp);
            }
        };

        DelayTime = ButtonWidget.builder(Text.literal("?"), button -> {}).dimensions(width / 2 + 205, 45, 10, 15).tooltip(Tooltip.of(Text.literal("受伤等待一段时间后强度开始下降\n再次受伤将会覆盖当前的等待时间\n非叠加,是覆盖"))).build();

        ADownTime = new SliderWidget(width / 2 + 5, 70, 100, 15, Text.literal("A强度下降间隔" + strengthConfig.getADownTime() * 50 + "ms"),  (double) strengthConfig.getADownTime() / 120) {
            @Override
            protected void updateMessage() {}

            @Override
            protected void applyValue() {
                int tmp = (int) (this.value * 120);
                if(tmp == 0) tmp = 1;
                this.setMessage(Text.literal("A强度下降间隔" + tmp * 50 + "ms"));
                strengthConfig.setADownTime(tmp);
            }
        };

        BDownTime = new SliderWidget(width / 2 + 105, 70, 100, 15, Text.literal("B强度下降间隔" + strengthConfig.getBDownTime() * 50 + "ms"),  (double) strengthConfig.getBDownTime() / 120) {
            @Override
            protected void updateMessage() {}

            @Override
            protected void applyValue() {
                int tmp = (int) (this.value * 120);
                if(tmp == 0) tmp = 1;
                this.setMessage(Text.literal("B强度下降间隔" + tmp * 50 + "ms"));
                strengthConfig.setBDownTime(tmp);
            }
        };

        DownTime = ButtonWidget.builder(Text.literal("?"), button -> {}).dimensions(width / 2 + 205, 70, 10, 15).tooltip(Tooltip.of(Text.literal("强度下降的时候每过此时间强度下降一次"))).build();

        ADownValue = new SliderWidget(width / 2 - 205, 70, 100, 15, Text.literal("A强度下降数值" + strengthConfig.getADownValue()),  (double) strengthConfig.getADownValue() / 20) {
            @Override
            protected void updateMessage() {}

            @Override
            protected void applyValue() {
                int tmp = (int) (this.value * 20);
                this.setMessage(Text.literal("A强度下降数值" + tmp));
                strengthConfig.setADownValue(tmp);
            }
        };

        BDownValue = new SliderWidget(width / 2 - 105, 70, 100, 15, Text.literal("A强度下降数值" + strengthConfig.getBDownValue()),  (double) strengthConfig.getBDownValue() / 20) {
            @Override
            protected void updateMessage() {}

            @Override
            protected void applyValue() {
                int tmp = (int) (this.value * 20);
                this.setMessage(Text.literal("B强度下降数值" + tmp));
                strengthConfig.setBDownValue(tmp);
            }
        };

        DownValue = ButtonWidget.builder(Text.literal("?"), button -> {}).dimensions(width / 2 - 215, 70, 10, 15).tooltip(Tooltip.of(Text.literal("每次强度下降的时候下降的数值"))).build();

        ADeathStrength = new SliderWidget(width / 2 - 205, 95, 100, 15, Text.literal("A死亡增加强度" + strengthConfig.getADeathStrength()),  (double) strengthConfig.getADeathStrength() / 70) {
            @Override
            protected void updateMessage() {}

            @Override
            protected void applyValue() {
                int tmp = (int) (this.value * 70);
                strengthConfig.setADeathStrength(tmp);
                this.setMessage(Text.literal("A死亡增加强度" + tmp));
            }
        };

        BDeathStrength = new SliderWidget(width / 2 - 105, 95, 100, 15, Text.literal("B死亡增加强度" + strengthConfig.getBDeathStrength()),  (double) strengthConfig.getBDeathStrength() / 70) {
            @Override
            protected void updateMessage() {}

            @Override
            protected void applyValue() {
                int tmp = (int) (this.value * 70);
                strengthConfig.setBDeathStrength(tmp);
                this.setMessage(Text.literal("B死亡增加强度" + tmp));
            }
        };

        DeathStrength = ButtonWidget.builder(Text.literal("?"), button -> {}).dimensions(width / 2 - 215, 95, 10, 15).tooltip(Tooltip.of(Text.literal("死亡时增加的强度\n计算完受伤强度后叠加\n和受伤强度同时作用\n死亡时将会发送\n死亡时收到的伤害x每伤害强度+死亡增加强度"))).build();

        ADeathDelay = new SliderWidget(width / 2 + 5, 95, 100, 15, Text.literal("A死亡时强度下降等待" + strengthConfig.getADeathDelay() * 50 + "ms"),  (double) strengthConfig.getADeathDelay() / 120) {
            @Override
            protected void updateMessage() {}

            @Override
            protected void applyValue() {
                int tmp = (int) (this.value * 120);
                this.setMessage(Text.literal("A下降等待" + tmp * 50 + "ms"));
                strengthConfig.setADeathDelay(tmp);
            }
        };

        BDeathDelay = new SliderWidget(width / 2 + 105, 95, 100, 15, Text.literal("B死亡时强度下降等待" + strengthConfig.getBDeathDelay() * 50 + "ms"),  (double) strengthConfig.getBDeathDelay() / 120) {
            @Override
            protected void updateMessage() {}

            @Override
            protected void applyValue() {
                int tmp = (int) (this.value * 120);
                this.setMessage(Text.literal("B下降等待" + tmp * 50 + "ms"));
                strengthConfig.setBDeathDelay(tmp);
            }
        };

        DeathDelay = ButtonWidget.builder(Text.literal("?"), button -> {}).dimensions(width / 2 + 205, 95, 10, 15).tooltip(Tooltip.of(Text.literal("同强度下降等待\n此值在死亡时生效\n非叠加,是覆盖"))).build();


        int width1 = client.getWindow().getScaledWidth(), height1 = client.getWindow().getScaledHeight();

        RenderingPositionX = new SliderWidget(width / 2 + 5, 120, 100, 15, Text.literal("强度显示位置X:" + modConfig.getRenderingPositionX()),  (double) modConfig.getRenderingPositionX() / width1) {
            @Override
            protected void updateMessage() {}

            @Override
            protected void applyValue() {
                int tmp = (int) (this.value * width1);
                modConfig.setRenderingPositionX(tmp);
                if(modConfig.getRenderingPositionX() >= width1 || modConfig.getRenderingPositionY() >= height1) {
                    this.setMessage(Text.literal("已关闭强度显示"));
                    RenderingPositionY.setMessage(Text.literal("已关闭强度显示"));
                }
                else {
                    this.setMessage(Text.literal("强度显示位置X:" + tmp));
                    RenderingPositionY.setMessage(Text.literal("强度显示位置Y:" + modConfig.getRenderingPositionY()));
                }
            }
        };

        RenderingPositionY = new SliderWidget(width / 2 + 105, 120, 100, 15, Text.literal("强度显示位置Y:" + modConfig.getRenderingPositionY()),  (double) modConfig.getRenderingPositionY() / height1) {
            @Override
            protected void updateMessage() {}

            @Override
            protected void applyValue() {
                int tmp = (int) (this.value * height1);
                modConfig.setRenderingPositionY(tmp);
                if(modConfig.getRenderingPositionX() >= width1 || modConfig.getRenderingPositionY() >= height1) {
                    this.setMessage(Text.literal("已关闭强度显示"));
                    RenderingPositionX.setMessage(Text.literal("已关闭强度显示"));
                }
                else {
                    this.setMessage(Text.literal("强度显示位置Y:" + tmp));
                    RenderingPositionX.setMessage(Text.literal("强度显示位置X:" + modConfig.getRenderingPositionX()));
                }
            }
        };
        
        button1 = ButtonWidget.builder(Text.literal("保存配置到文件"), button -> {
                    strengthConfig.savaFile();
                    modConfig.savaFile();
                })
                .dimensions(width / 2 - 205, 20, 200, 15).tooltip(Tooltip.of(Text.literal("所有更改是临时更改\n点击此按钮保存到文件"))).build();
        button2 = ButtonWidget.builder(Text.literal("连接设置"), button -> {
                    client.setScreen(WebSocketConfigScreen);
                })
                .dimensions(width / 2 + 5, 20, 200, 15).tooltip(Tooltip.of(Text.literal("点击修改详细连接设置\n非必要无需修改"))).build();

        createQR = ButtonWidget.builder(Text.literal("创建连接二维码并打开"), button -> {
            ToolQR.CreateQR();
        }).dimensions(width / 2 - 205, 120, 200, 15).tooltip(Tooltip.of(Text.literal("图片默认生成于此地址:\n" + System.getProperty("user.dir")))).build();


//        textFieldWidget = new TextFieldWidget(this.textRenderer,width / 2 -205, 170, 200, 15, Text.literal("Enter text..."));
//        textFieldWidget.setChangedListener(this::test);
//
//        textFieldWidget.setPlaceholder(Text.literal("占位字符串").setStyle(Style.EMPTY.withColor(Formatting.GRAY)));


        addDrawableChild(button1);
        addDrawableChild(button2);
        addDrawableChild(ADamageStrength);
        addDrawableChild(BDamageStrength);
        addDrawable(DamageStrength);
        addDrawableChild(ADelayTime);
        addDrawableChild(BDelayTime);
        addDrawable(DelayTime);
        addDrawableChild(ADownTime);
        addDrawableChild(BDownTime);
        addDrawable(DownTime);
        addDrawableChild(ADownValue);
        addDrawableChild(BDownValue);
        addDrawable(DownValue);
        addDrawableChild(ADeathStrength);
        addDrawableChild(BDeathStrength);
        addDrawable(DeathStrength);
        addDrawableChild(ADeathDelay);
        addDrawableChild(BDeathDelay);
        addDrawable(DeathDelay);
        addDrawableChild(createQR);
        addDrawableChild(RenderingPositionX);
        addDrawableChild(RenderingPositionY);
//        addDrawableChild(textFieldWidget);
    }

    public void test(String text){
        System.out.println(text);
    }



}