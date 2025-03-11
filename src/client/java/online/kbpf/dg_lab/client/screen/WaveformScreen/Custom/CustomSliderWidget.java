package online.kbpf.dg_lab.client.screen.WaveformScreen.Custom;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.objectweb.asm.tree.analysis.Value;

import static online.kbpf.dg_lab.client.screen.WaveformScreen.Custom.CustomScreen.list;

public abstract class CustomSliderWidget extends SliderWidget {

    private boolean sliderFocused;
    private static final Identifier TEXTURE = Identifier.ofVanilla("widget/slider");
    private static final Identifier HIGHLIGHTED_TEXTURE = Identifier.ofVanilla("widget/slider_highlighted");
    private static final Identifier HANDLE_TEXTURE = Identifier.ofVanilla("widget/slider_handle");
    private static final Identifier HANDLE_HIGHLIGHTED_TEXTURE = Identifier.ofVanilla("widget/slider_handle_highlighted");

    public CustomSliderWidget(int x, int y, int width, int height, Text text, double value) {
        super(x, y, width, height, text, value);
    }

    public void setValue(int value){

        this.setMessage(Text.literal(String.valueOf(value)));
        this.value = (double) value / 100;
    }

    @Override
    public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {

        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        context.setShaderColor(1.0F, 1.0F, 1.0F, this.alpha);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        context.drawGuiTexture(this.getTexture(), this.getX(), this.getY(), this.getWidth(), this.getHeight());
        context.drawGuiTexture(this.getHandleTexture(), this.getX() + (int)(this.value * (double)(this.width - 8)), this.getY(), 8, this.getHeight());
        context.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        int i = this.active ? 16777215 : 10526880;
//        this.drawScrollableText(context, minecraftClient.textRenderer, 2, i | MathHelper.ceil(this.alpha * 255.0F) << 24);
        context.drawTextWithShadow(minecraftClient.textRenderer, this.getMessage(), this.getX() + this.getWidth(), this.getY(), i | MathHelper.ceil(this.alpha * 255.0F) << 24);


    }


    private Identifier getTexture() {
        return this.isFocused() && !this.sliderFocused ? HIGHLIGHTED_TEXTURE : TEXTURE;
    }

    private Identifier getHandleTexture() {
        return !this.hovered && !this.sliderFocused ? HANDLE_TEXTURE : HANDLE_HIGHLIGHTED_TEXTURE;
    }




    @Override
    protected abstract void updateMessage();

    @Override
    protected abstract void applyValue();


}
