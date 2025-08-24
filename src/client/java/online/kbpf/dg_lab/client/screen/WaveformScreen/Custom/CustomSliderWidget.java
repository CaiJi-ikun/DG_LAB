package online.kbpf.dg_lab.client.screen.WaveformScreen.Custom;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

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

    public void setValue(int value) {
        this.setMessage(Text.literal(String.valueOf(value)));
        this.value = (double) value / 100;
    }

    @Override
    public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        MinecraftClient minecraftClient = MinecraftClient.getInstance();

        // 使用预定义的 RenderPipeline - 从源码中可以看到有 RenderPipelines.GUI
        com.mojang.blaze3d.pipeline.RenderPipeline renderPipeline = net.minecraft.client.gl.RenderPipelines.GUI;

        // 滑块背景
        context.drawTexture(
                renderPipeline,              // RenderPipeline
                this.getTexture(),           // Identifier texture
                this.getX(),                 // int x
                this.getY(),                 // int y
                0.0F,                        // float u
                0.0F,                        // float v
                this.getWidth(),             // int width
                this.getHeight(),            // int height
                this.getWidth(),             // int regionWidth
                this.getHeight(),            // int regionHeight
                0                            // int z
        );

        // 滑块手柄
        context.drawTexture(
                renderPipeline,              // RenderPipeline
                this.getHandleTexture(),     // Identifier texture
                this.getX() + (int)(this.value * (double)(this.width - 8)), // int x
                this.getY(),                 // int y
                0.0F,                        // float u
                0.0F,                        // float v
                8,                           // int width
                this.getHeight(),            // int height
                8,                           // int regionWidth
                this.getHeight(),            // int regionHeight
                0                            // int z
        );

        int i = this.active ? 16777215 : 10526880;
        context.drawTextWithShadow(
                minecraftClient.textRenderer,
                this.getMessage(),
                this.getX() + this.getWidth(),
                this.getY(),
                i | MathHelper.ceil(this.alpha * 255.0F) << 24
        );
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