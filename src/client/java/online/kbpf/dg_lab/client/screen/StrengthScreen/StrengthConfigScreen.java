package online.kbpf.dg_lab.client.screen.StrengthScreen;

import online.kbpf.dg_lab.client.Dg_labClient;
import online.kbpf.dg_lab.client.Config.ModConfig;
import online.kbpf.dg_lab.client.Config.StrengthConfig;
import online.kbpf.dg_lab.client.screen.ConfigScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;

@Environment(EnvType.CLIENT)
public class StrengthConfigScreen extends Screen {


    private SliderWidget ADamageStrength, BDamageStrength;
    private ButtonWidget DamageStrength;
    private SliderWidget ADelayTime, BDelayTime;
    private ButtonWidget DelayTime;
    private SliderWidget ADownTime;
    private SliderWidget BDownTime;
    private ButtonWidget DownTime;
    private SliderWidget ADownValue;
    private SliderWidget BDownValue;
    private ButtonWidget DownValue;
    private SliderWidget ADeathStrength;
    private SliderWidget BDeathStrength;
    private ButtonWidget DeathStrength;
    private SliderWidget ADeathDelay;
    private SliderWidget BDeathDelay;
    private ButtonWidget DeathDelay;

    public StrengthConfigScreen() {
        // 此参数为屏幕的标题，进入屏幕中，复述功能会复述。
        super(Text.literal("强度配置界面"));
    }


    @Override
    public void close() {
        Screen configScreen = new ConfigScreen();
        client.setScreen(configScreen);
    }

    @Override
    protected void init() {

        StrengthConfig strengthConfig = Dg_labClient.getStrengthConfig();
        ModConfig modConfig = Dg_labClient.getModConfig();
        MinecraftClient client = MinecraftClient.getInstance();
        ADamageStrength = new SliderWidget(width / 2 - 205, 20, 100, 15, Text.literal("A每伤害强度" + String.format("%.2f", strengthConfig.getADamageStrength())), strengthConfig.getADamageStrength() / 20) {
            @Override
            protected void updateMessage() {
            }

            @Override
            protected void applyValue() {
                float ADamageStrength = (float) (this.value * 20);
                strengthConfig.setADamageStrength(ADamageStrength);
                this.setMessage(Text.literal("A每伤害强度" + String.format("%.2f", strengthConfig.getADamageStrength())));
            }
        };

        BDamageStrength = new SliderWidget(width / 2 - 105, 20, 100, 15, Text.literal("B每伤害强度" + String.format("%.2f", strengthConfig.getBDamageStrength())), strengthConfig.getBDamageStrength() / 20) {
            @Override
            protected void updateMessage() {
            }

            @Override
            protected void applyValue() {
                float BDamageStrength = (float) (this.value * 20);
                strengthConfig.setBDamageStrength(BDamageStrength);
                this.setMessage(Text.literal("B每伤害强度" + String.format("%.2f", strengthConfig.getBDamageStrength())));
            }
        };

        DamageStrength = ButtonWidget.builder(Text.literal("?"), button -> {
        }).dimensions(width / 2 - 215, 20, 10, 15).tooltip(Tooltip.of(Text.literal("每受到半颗心伤害增加的强度\n受伤时增加强度若小于1则增加1\n大于一的强度数值9舍0入\n若为0则不增加"))).build();

        ADelayTime = new SliderWidget(width / 2 + 5, 20, 100, 15, Text.literal("A强度下降等待" + strengthConfig.getADelayTime() * 50 + "ms"), (double) strengthConfig.getADelayTime() / 120) {
            @Override
            protected void updateMessage() {
            }

            @Override
            protected void applyValue() {
                int tmp = (int) (this.value * 120);
                this.setMessage(Text.literal("A强度下降等待" + tmp * 50 + "ms"));
                strengthConfig.setADelayTime(tmp);
            }
        };

        BDelayTime = new SliderWidget(width / 2 + 105, 20, 100, 15, Text.literal("B强度下降等待" + strengthConfig.getBDelayTime() * 50 + "ms"), (double) strengthConfig.getBDelayTime() / 120) {
            @Override
            protected void updateMessage() {
            }

            @Override
            protected void applyValue() {
                int tmp = (int) (this.value * 120);
                this.setMessage(Text.literal("B强度下降等待" + tmp * 50 + "ms"));
                strengthConfig.setBDelayTime(tmp);
            }
        };

        DelayTime = ButtonWidget.builder(Text.literal("?"), button -> {
        }).dimensions(width / 2 + 205, 20, 10, 15).tooltip(Tooltip.of(Text.literal("受伤等待一段时间后强度开始下降\n再次受伤将会覆盖当前的等待时间\n非叠加,是覆盖"))).build();

        ADownTime = new SliderWidget(width / 2 + 5, 45, 100, 15, Text.literal("A强度下降间隔" + strengthConfig.getADownTime() * 50 + "ms"), (double) strengthConfig.getADownTime() / 120) {
            @Override
            protected void updateMessage() {
            }

            @Override
            protected void applyValue() {
                int tmp = (int) (this.value * 120);
                if (tmp == 0) tmp = 1;
                this.setMessage(Text.literal("A强度下降间隔" + tmp * 50 + "ms"));
                strengthConfig.setADownTime(tmp);
            }
        };

        BDownTime = new SliderWidget(width / 2 + 105, 45, 100, 15, Text.literal("B强度下降间隔" + strengthConfig.getBDownTime() * 50 + "ms"), (double) strengthConfig.getBDownTime() / 120) {
            @Override
            protected void updateMessage() {
            }

            @Override
            protected void applyValue() {
                int tmp = (int) (this.value * 120);
                if (tmp == 0) tmp = 1;
                this.setMessage(Text.literal("B强度下降间隔" + tmp * 50 + "ms"));
                strengthConfig.setBDownTime(tmp);
            }
        };

        DownTime = ButtonWidget.builder(Text.literal("?"), button -> {
        }).dimensions(width / 2 + 205, 45, 10, 15).tooltip(Tooltip.of(Text.literal("强度下降的时候每过此时间强度下降一次"))).build();

        ADownValue = new SliderWidget(width / 2 - 205, 45, 100, 15, Text.literal("A强度下降数值" + strengthConfig.getADownValue()), (double) strengthConfig.getADownValue() / 20) {
            @Override
            protected void updateMessage() {
            }

            @Override
            protected void applyValue() {
                int tmp = (int) (this.value * 20);
                this.setMessage(Text.literal("A强度下降数值" + tmp));
                strengthConfig.setADownValue(tmp);
            }
        };

        BDownValue = new SliderWidget(width / 2 - 105, 45, 100, 15, Text.literal("A强度下降数值" + strengthConfig.getBDownValue()), (double) strengthConfig.getBDownValue() / 20) {
            @Override
            protected void updateMessage() {
            }

            @Override
            protected void applyValue() {
                int tmp = (int) (this.value * 20);
                this.setMessage(Text.literal("B强度下降数值" + tmp));
                strengthConfig.setBDownValue(tmp);
            }
        };

        DownValue = ButtonWidget.builder(Text.literal("?"), button -> {
        }).dimensions(width / 2 - 215, 45, 10, 15).tooltip(Tooltip.of(Text.literal("每次强度下降的时候下降的数值"))).build();

        ADeathStrength = new SliderWidget(width / 2 - 205, 70, 100, 15, Text.literal("A死亡增加强度" + strengthConfig.getADeathStrength()), (double) strengthConfig.getADeathStrength() / 200) {
            @Override
            protected void updateMessage() {
            }

            @Override
            protected void applyValue() {
                int tmp = (int) (this.value * 200);
                strengthConfig.setADeathStrength(tmp);
                this.setMessage(Text.literal("A死亡增加强度" + tmp));
            }
        };

        BDeathStrength = new SliderWidget(width / 2 - 105, 70, 100, 15, Text.literal("B死亡增加强度" + strengthConfig.getBDeathStrength()), (double) strengthConfig.getBDeathStrength() / 200) {
            @Override
            protected void updateMessage() {
            }

            @Override
            protected void applyValue() {
                int tmp = (int) (this.value * 200);
                strengthConfig.setBDeathStrength(tmp);
                this.setMessage(Text.literal("B死亡增加强度" + tmp));
            }
        };

        DeathStrength = ButtonWidget.builder(Text.literal("?"), button -> {
        }).dimensions(width / 2 - 215, 70, 10, 15).tooltip(Tooltip.of(Text.literal("死亡时增加的强度\n计算完受伤强度后叠加\n和受伤强度同时作用\n死亡时将会发送\n死亡时收到的伤害x每伤害强度+死亡增加强度"))).build();

        ADeathDelay = new SliderWidget(width / 2 + 5, 70, 100, 15, Text.literal("A死亡时强度下降等待" + strengthConfig.getADeathDelay() * 50 + "ms"), (double) strengthConfig.getADeathDelay() / 120) {
            @Override
            protected void updateMessage() {
            }

            @Override
            protected void applyValue() {
                int tmp = (int) (this.value * 120);
                this.setMessage(Text.literal("A下降等待" + tmp * 50 + "ms"));
                strengthConfig.setADeathDelay(tmp);
            }
        };

        BDeathDelay = new SliderWidget(width / 2 + 105, 70, 100, 15, Text.literal("B死亡时强度下降等待" + strengthConfig.getBDeathDelay() * 50 + "ms"), (double) strengthConfig.getBDeathDelay() / 120) {
            @Override
            protected void updateMessage() {
            }

            @Override
            protected void applyValue() {
                int tmp = (int) (this.value * 120);
                this.setMessage(Text.literal("B下降等待" + tmp * 50 + "ms"));
                strengthConfig.setBDeathDelay(tmp);
            }
        };

        DeathDelay = ButtonWidget.builder(Text.literal("?"), button -> {
        }).dimensions(width / 2 + 205, 70, 10, 15).tooltip(Tooltip.of(Text.literal("同强度下降等待\n此值在死亡时生效\n非叠加,是覆盖"))).build();


        int width1 = client.getWindow().getScaledWidth(), height1 = client.getWindow().getScaledHeight();


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
    }


}