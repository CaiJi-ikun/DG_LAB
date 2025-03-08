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
import online.kbpf.dg_lab.client.Tool.DGWaveformTool;


import java.util.List;

import static online.kbpf.dg_lab.client.Dg_labClient.*;

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
        private final ButtonWidget copyButton, pasteButton, testButton;          //按钮
        private final TextRenderer textRenderer;        //文本渲染参数
        private final Text text;                        //文本

        public Entry(TextRenderer textRenderer, Text text, String name) {
            //设置单个项目相关内容


            waveformDataText = new TextFieldWidget(textRenderer, 100, 15, Text.literal(""));
            waveformDataText.setMaxLength(100000);
            if(waveformDataMap.containsKey(name)){
                waveformDataText.setText(waveformDataMap.get(name));
            }


            waveformDataText.setPlaceholder(Text.literal("输入波形代码").withColor(0xaaaaaa));

            waveformDataText.setChangedListener(inputText -> {
                if(waveformDataMap.containsKey(name)){
                    waveformDataMap.replace(name, inputText);
                    waveformDuration.replace(name, DGWaveformTool.checkAndCountValidSubstrings(inputText));
                }
                else {
                    waveformDataMap.put(name, inputText);
                    waveformDuration.put(name, DGWaveformTool.checkAndCountValidSubstrings(inputText));
                }

            });

            copyButton = new ButtonWidget.Builder(Text.literal("\uD83D\uDCC4"), button -> {
                MinecraftClient.getInstance().keyboard.setClipboard(waveformDataText.getText());
                waveformDataText.setFocused(false);
                button.setFocused(false);
            }).tooltip(Tooltip.of(Text.literal("点击复制波形代码"))).build();

            pasteButton = new ButtonWidget.Builder(Text.literal("\uD83D\uDCCB"), button -> {
                String clipboardText = MinecraftClient.getInstance().keyboard.getClipboard();
                waveformDataText.setText(clipboardText);
                waveformDataText.setFocused(false);
                button.setFocused(false);
            }).tooltip(Tooltip.of(Text.literal("点击粘贴波形代码"))).build();

            testButton = new ButtonWidget.Builder(Text.literal("\uD83D\uDCE8"), button -> {
                webSocketServer.sendDGWaveForm(waveformDataText.getText(), 1);
            }).tooltip(Tooltip.of(Text.literal("发送到终端1通道"))).build();


            this.textRenderer = textRenderer;
            this.text = text;

        }


        //确保点击/交互被正确传递
        @Override
        public List<? extends Selectable> selectableChildren() {
            return List.of(waveformDataText, copyButton, pasteButton, testButton);
        }

        @Override
        public List<? extends Element> children() {
            return List.of(waveformDataText, copyButton, pasteButton, testButton);
        }


        @Override
        public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            //渲染相关
            //文本框位置宽高
            waveformDataText.setDimensionsAndPosition(x + (int) (entryWidth * 0.4), 20, x + (int) (entryWidth * 0.4), y);
            waveformDataText.render(context, mouseX, mouseY, tickDelta);

            copyButton.setDimensionsAndPosition(15, 20, waveformDataText.getX() + waveformDataText.getWidth(), y);
            copyButton.render(context, mouseX, mouseY, tickDelta);


            pasteButton.setDimensionsAndPosition(15, 20, copyButton.getX() + 15, y);
            pasteButton.render(context, mouseX, mouseY, tickDelta);

            testButton.setDimensionsAndPosition(15, 20, pasteButton.getX() + 15, y);
            testButton.render(context, mouseX, mouseY, tickDelta);


            context.drawTextWithShadow(textRenderer, this.text, x + (int) (entryWidth * 0.2), y + 5, 0xffffff);

            int duration = DGWaveformTool.checkAndCountValidSubstrings(waveformDataText.getText());
            if(duration == 0)
                context.drawTextWithShadow(textRenderer, "ERROR", testButton.getX() + 15, y + 5, 0xFF0000);
            else context.drawTextWithShadow(textRenderer, (duration * 100) + "ms", testButton.getX() + 15, y + 5, 0xFFFFFF);

        }


    }

}
