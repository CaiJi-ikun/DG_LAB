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

import static online.kbpf.dg_lab.client.Dg_labClient.waveformDataMap;

public class WaveformListWidget extends ElementListWidget<WaveformListWidget.Entry> {

    //列表项目内容
    private final int width;


    public WaveformListWidget(MinecraftClient minecraftClient, int width, int height, int y, int itemHeight) {
        super(minecraftClient, width, height, y, itemHeight);
        this.width = width;
    }


    //修改左右宽度
    @Override
    public int getRowLeft() {
        return this.getX(); // 从屏幕最左侧开始
    }
    @Override
    public int getRowWidth() {
        return this.width; // 宽度设置为屏幕宽度
    }
    @Override
    protected int getScrollbarX() {
        return this.getRight() - 6; // 滚动条紧贴右侧
    }


    public void addWaveformEntry(Entry entry) {
        this.addEntry(entry);
    }

    public static class Entry extends ElementListWidget.Entry<Entry> {
        private final TextFieldWidget waveformDataText; //文字输入框
        private final ButtonWidget copyButton, pasteButton;          //按钮
        private final TextRenderer textRenderer;        //文本渲染参数
        private final Text text;                        //文本

        public Entry(TextRenderer textRenderer, Text text, String name) {
            //设置单个项目相关内容


            waveformDataText = new TextFieldWidget(textRenderer, 100, 15, Text.literal(""));
            if(waveformDataMap.containsKey(name)){
                waveformDataText.setText(waveformDataMap.get(name));
            }


            waveformDataText.setPlaceholder(Text.literal("输入波形代码").withColor(0xaaaaaa));

            waveformDataText.setChangedListener(inputText -> {
                if(waveformDataMap.containsKey(name)){
                    waveformDataMap.replace(name, inputText);
                }
                else {
                    waveformDataMap.put(name, inputText);
                }

            });

            copyButton = new ButtonWidget.Builder(Text.literal("\uD83D\uDCC4"), button -> {
                System.out.println("test");
                MinecraftClient.getInstance().keyboard.setClipboard(waveformDataText.getText());
                waveformDataText.setFocused(false);
                button.setFocused(false);
            }).tooltip(Tooltip.of(Text.literal("点击复制波形代码"))).build();

            pasteButton = new ButtonWidget.Builder(Text.literal("\uD83D\uDCCB"), button -> {
                System.out.println("test");
                String clipboardText = MinecraftClient.getInstance().keyboard.getClipboard();
                System.out.println(clipboardText);
                waveformDataText.setText(clipboardText);
                waveformDataText.setFocused(false);
                button.setFocused(false);
            }).tooltip(Tooltip.of(Text.literal("点击粘贴波形代码"))).build();

            this.textRenderer = textRenderer;
            this.text = text;

        }

        @Override
        public List<? extends Selectable> selectableChildren() {
            return List.of(waveformDataText, copyButton, pasteButton);
        }

        @Override
        public List<? extends Element> children() {
            return List.of(waveformDataText, copyButton, pasteButton);
        }


        @Override
        public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            //渲染相关
            waveformDataText.setX(x + (int) (entryWidth * 0.5));
            waveformDataText.setY(y);
            waveformDataText.setWidth((int) (entryWidth * 0.3));
            waveformDataText.setHeight(15);
            waveformDataText.render(context, mouseX, mouseY, tickDelta);

            copyButton.setX(waveformDataText.getX() + waveformDataText.getWidth());
            copyButton.setY(y);
            copyButton.setWidth(15);
            copyButton.setHeight(15);
            copyButton.render(context, mouseX, mouseY, tickDelta);


            pasteButton.setX(copyButton.getX() + 15);
            pasteButton.setY(y);
            pasteButton.setWidth(15);
            pasteButton.setHeight(15);
            pasteButton.render(context, mouseX, mouseY, tickDelta);


            context.drawTextWithShadow(textRenderer, this.text, x + (int) (entryWidth * 0.2), y + 5, 0xffffff);


        }


    }

}
