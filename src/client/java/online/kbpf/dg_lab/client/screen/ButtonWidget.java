package online.kbpf.dg_lab.client.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;


public class ButtonWidget extends net.minecraft.client.gui.widget.ButtonWidget {
    public ButtonWidget(int x, int y, int width, int height, Text message, PressAction onPress) {
        super(x, y, width, height, message, onPress);
    }

    public ButtonWidget(int x, int y, int width, int height, Text message, PressAction onPress, TooltipSupplier tooltipSupplier) {
        super(x, y, width, height, message, onPress, tooltipSupplier);
    }

    public static Builder builder(Text message, PressAction onPress) {
        return new Builder(message, onPress);
    }


    public static class Builder {
        private final Text message;
        private final PressAction onPress;
        private Text tooltip;
        private int x;
        private int y;
        private int width = 150;
        private int height = 20;
//        private NarrationSupplier narrationSupplier;

        public Builder(Text message, PressAction onPress) {
//            this.narrationSupplier = net.minecraft.client.gui.widget.ButtonWidget.DEFAULT_NARRATION_SUPPLIER;
            this.message = message;
            this.onPress = onPress;
        }

        public Builder position(int x, int y) {
            this.x = x;
            this.y = y;
            return this;
        }

        public Builder width(int width) {
            this.width = width;
            return this;
        }

        public Builder size(int width, int height) {
            this.width = width;
            this.height = height;
            return this;
        }

        public Builder dimensions(int x, int y, int width, int height) {
            return this.position(x, y).size(width, height);
        }

        public Builder tooltip(Text tooltip) {
            this.tooltip = tooltip;
            return this;
        }

//        public Builder narrationSupplier(NarrationSupplier narrationSupplier) {
//            this.narrationSupplier = narrationSupplier;
//            return this;
//        }

        public ButtonWidget build() {

            if(tooltip != null) {
                ButtonWidget buttonWidget = new ButtonWidget(this.x, this.y, this.width, this.height, this.message, this.onPress, (btn, matrices, mouseX, mouseY) -> {
                    // 在这里渲染tooltip
                    MinecraftClient client = MinecraftClient.getInstance();
                    if (client.currentScreen != null) {
                        client.currentScreen.renderTooltip(
                                matrices,
                                tooltip,
                                mouseX, mouseY
                        );
                    }
                });
                return buttonWidget;
            }

            ButtonWidget buttonWidget = new ButtonWidget(this.x, this.y, this.width, this.height, this.message, this.onPress);
            return buttonWidget;

        }
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
