package online.kbpf.dg_lab.client.screen.WaveformScreen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;



import java.util.List;

public class WaveformListWidget extends ElementListWidget<WaveformListWidget.Entry> {

    public WaveformListWidget(MinecraftClient minecraftClient, int width, int height, int y, int itemHeight) {
        super(minecraftClient, width, height, y, itemHeight);
    }

    public void addWaveformEntry(Entry entry) {
        this.addEntry(entry);
    }

    public static class Entry extends ElementListWidget.Entry<Entry> {
        private final TextFieldWidget waveformDataText;
        private final ButtonWidget sendButton;
        private final TextRenderer textRenderer;
        private final Text text;

        public Entry(TextRenderer textRenderer, Text text, Runnable runnable) {
            waveformDataText = new TextFieldWidget(textRenderer, 100, 15, Text.literal("输入波形代码"));
            waveformDataText.setPlaceholder(Text.literal("输入波形代码").withColor(0xaaaaaa));
            sendButton = new ButtonWidget.Builder(Text.literal("❏"), button -> {
                MinecraftClient.getInstance().keyboard.setClipboard(waveformDataText.getText());
            }).tooltip(Tooltip.of(Text.literal("点击复制波形代码"))).build();
            this.textRenderer = textRenderer;
            this.text = text;

        }

        @Override
        public List<? extends Selectable> selectableChildren() {
            return List.of(waveformDataText, sendButton);
        }

        @Override
        public List<? extends Element> children() {
            return List.of(waveformDataText, sendButton);
        }


        @Override
        public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            waveformDataText.setX((int) (x + (entryWidth / 2.5)));
            waveformDataText.setY(y);
            waveformDataText.setWidth(entryWidth / 2);
            waveformDataText.setHeight(15);
            waveformDataText.render(context, mouseX, mouseY, tickDelta);

            sendButton.setX((int) (x + (entryWidth / 2.5) + ((double) entryWidth * 0.51)));
            sendButton.setY(y);
            sendButton.setWidth(15);
            sendButton.setHeight(15);
            sendButton.render(context, mouseX, mouseY, tickDelta);


            context.drawTextWithShadow(textRenderer, this.text, x, y + 5, 0xffffff);

//            System.out.println(x + " " + y + " " + entryWidth + " " + entryHeight + "a");
        }


    }

}
