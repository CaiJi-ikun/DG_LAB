package buzz.kbpf.dg_lab.client.screen;


import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

@Environment(EnvType.CLIENT)
public class screenMain extends Screen {





    public ButtonWidget button1;
    public ButtonWidget button2;

    protected screenMain(Text title) {
        super(title);
    }

    @Override
    protected void init() {
        button1 = ButtonWidget.builder(Text.literal("按钮 1"), button -> {
                    System.out.println("你点击了按钮 1！");
                })
                .dimensions(width / 2 - 205, 20, 200, 20)
                .tooltip(Tooltip.of(Text.literal("按钮 1 的提示")))
                .build();
        button2 = ButtonWidget.builder(Text.literal("按钮 2"), button -> {
                    System.out.println("你点击了按钮 2！");
                })
                .dimensions(width / 2 + 5, 20, 200, 20)
                .tooltip(Tooltip.of(Text.literal("按钮 2 的提示")))
                .build();

        addDrawableChild(button1);
        addDrawableChild(button2);
    }
}