package online.kbpf.dg_lab.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public abstract class SliderWidget extends net.minecraft.client.gui.widget.SliderWidget {
    public SliderWidget(int x, int y, int width, int height, Text text, double value) {
        super(x, y, width, height, text, value);
    }


    private boolean sliderFocused;
    private static final Identifier TEXTURE = new Identifier("textures/gui/slider.png");

    private int getYImage() {
        int i = this.isFocused() && !this.sliderFocused ? 1 : 0;
        return i * 20;
    }

    private int getTextureV() {
        int i = !this.hovered && !this.sliderFocused ? 2 : 3;
        return i * 20;
    }


    @Override
    public void renderButton(DrawContext context, int mouseX, int mouseY, float delta) {
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        context.setShaderColor(1.0F, 1.0F, 1.0F, this.alpha);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        context.drawNineSlicedTexture(TEXTURE, this.getX(), this.getY(), this.getWidth(), this.getHeight(), 20, 4, 200, 20, 0, this.getYImage());
        context.drawNineSlicedTexture(TEXTURE, this.getX() + (int)(this.value * (double)(this.width - 8)), this.getY(), 8,  this.getHeight(), 20, 4, 200, 20, 0, this.getTextureV());
        context.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        int i = this.active ? 16777215 : 10526880;
        this.drawScrollableText(context, minecraftClient.textRenderer, 2, i | MathHelper.ceil(this.alpha * 255.0F) << 24);
    }
}
