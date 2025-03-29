package online.kbpf.dg_lab.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
//import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
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


    public void setPosition(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int getX(){
        return this.x;
    }

    public int getY(){
        return this.y;
    }

    @Override
    protected void renderBackground(MatrixStack matrices, MinecraftClient client, int mouseX, int mouseY) {
        RenderSystem.setShaderTexture(0, WIDGETS_TEXTURE);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        int i = (this.isHovered() ? 2 : 1) * 20;
        this.drawTexture(matrices, this.x + (int)(this.value * (double)(this.width - 8)), this.y, 0, 46 + i, 4, this.height);
        this.drawTexture(matrices, this.x + (int)(this.value * (double)(this.width - 8)) + 4, this.y, 196, 46 + i, 4, this.height);
    }



}
