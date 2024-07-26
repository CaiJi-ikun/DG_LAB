package buzz.kbpf.dg_lab.client.screen;

import buzz.kbpf.dg_lab.client.Dg_labClient;
import buzz.kbpf.dg_lab.client.entity.StrengthConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
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

    public SliderWidget ADamageStrength;
    public SliderWidget BDamageStrength;
    public SliderWidget ADelayTime;
    public SliderWidget BDelayTime;

    @Override
    protected void init() {
        StrengthConfig strengthConfig = Dg_labClient.getConfig();
        ADamageStrength = new SliderWidget(width / 2 - 205, 50, 100, 20, Text.literal("A每伤害强度" + String.format("%.2f", strengthConfig.getADamageStrength())),  strengthConfig.getADamageStrength() / 20) {
            @Override
            protected void updateMessage() {}

            @Override
            protected void applyValue() {
                float ADamageStrength = (float)this.value * 20;
                strengthConfig.setADamageStrength(ADamageStrength);
                this.setMessage(Text.literal("A每伤害强度" + String.format("%.2f", strengthConfig.getADamageStrength())));
            }
        };

        BDamageStrength = new SliderWidget(width / 2 - 105, 50, 100, 20, Text.literal("B每伤害强度" + String.format("%.2f", strengthConfig.getBDamageStrength())),  strengthConfig.getBDamageStrength() / 20) {
            @Override
            protected void updateMessage() {}

            @Override
            protected void applyValue() {
                float BDamageStrength = (float)this.value * 20;
                strengthConfig.setBDamageStrength(BDamageStrength);
                this.setMessage(Text.literal("B每伤害强度" + String.format("%.2f", strengthConfig.getBDamageStrength())));
            }
        };

        ADelayTime = new SliderWidget(width / 2 + 5, 50, 100, 20, Text.literal("A强度下降等待" + strengthConfig.getADelayTime() * 40 + "ms"),  (double) strengthConfig.getADelayTime() / 125) {
            @Override
            protected void updateMessage() {}

            @Override
            protected void applyValue() {
                float ADelayTime = (float)this.value * 125;
                this.setMessage(Text.literal("A强度下降等待" + (int)ADelayTime * 40 + "ms"));
                strengthConfig.setADelayTime((int) ADelayTime);
            }
        };

        BDelayTime = new SliderWidget(width / 2 + 105, 50, 100, 20, Text.literal("B强度下降等待" + strengthConfig.getBDelayTime() * 40 + "ms"),  (double) strengthConfig.getBDelayTime() / 125) {
            @Override
            protected void updateMessage() {}

            @Override
            protected void applyValue() {
                float BDelayTime = (float)this.value * 125;
                this.setMessage(Text.literal("B强度下降等待" + (int)BDelayTime * 40 + "ms"));
                strengthConfig.setBDelayTime((int) BDelayTime);
            }
        };
        
        button1 = ButtonWidget.builder(Text.literal("保存配置到文件"), button -> {
                    Dg_labClient.setConfig(strengthConfig);
                    Dg_labClient.saveConfig();
                })
                .dimensions(width / 2 - 205, 20, 200, 20).build();
        button2 = ButtonWidget.builder(Text.literal("按钮 2"), button -> {
                    System.out.println("你点击了按钮 2！");
                })
                .dimensions(width / 2 + 5, 20, 200, 20).build();

        addDrawableChild(button1);
        addDrawableChild(button2);
        addDrawableChild(ADamageStrength);
        addDrawableChild(BDamageStrength);
        addDrawableChild(ADelayTime);
        addDrawableChild(BDelayTime);
    }
}