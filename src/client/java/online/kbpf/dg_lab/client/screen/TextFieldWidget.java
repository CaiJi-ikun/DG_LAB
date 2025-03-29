package online.kbpf.dg_lab.client.screen;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

public class TextFieldWidget extends net.minecraft.client.gui.widget.TextFieldWidget {
    public TextFieldWidget(TextRenderer textRenderer, int x, int y, int width, int height, Text text) {
        super(textRenderer, x, y, width, height, text);
    }

    public TextFieldWidget(TextRenderer textRenderer, int x, int y, int width, int height, @Nullable net.minecraft.client.gui.widget.TextFieldWidget copyFrom, Text text) {
        super(textRenderer, x, y, width, height, copyFrom, text);
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



}
