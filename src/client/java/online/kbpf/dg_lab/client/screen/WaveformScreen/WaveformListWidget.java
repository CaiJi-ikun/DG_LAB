package online.kbpf.dg_lab.client.screen.WaveformScreen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
//import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.Screen;
//import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ElementListWidget;
//import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import online.kbpf.dg_lab.client.Tool.DGWaveformTool;
import online.kbpf.dg_lab.client.entity.Waveform.Waveform;
import online.kbpf.dg_lab.client.screen.ButtonWidget;
import online.kbpf.dg_lab.client.screen.TextFieldWidget;
import online.kbpf.dg_lab.client.screen.WaveformScreen.Custom.CustomScreen;


import java.util.List;

import static online.kbpf.dg_lab.client.screen.ConfigScreen.*;
import static online.kbpf.dg_lab.client.Dg_labClient.waveformMap;
import static online.kbpf.dg_lab.client.Dg_labClient.webSocketServer;

public class WaveformListWidget extends ElementListWidget<WaveformListWidget.Entry> {

    //列表项目内容
    private final int width;


    public WaveformListWidget(MinecraftClient minecraftClient, int width, int height, int top, int bottom, int itemHeight) {
        super(minecraftClient, width, height, top, bottom, itemHeight);
        System.out.println(width);
        this.width = width;
//        this.width = minecraftClient.getWindow().getWidth();
        this.left = 0;
    }


    //修改左右宽度
    @Override
    public int getRowLeft() {
        return this.left; // 从屏幕最左侧开始
    }
    @Override
    public int getRowWidth() {
        return this.width; // 宽度设置为屏幕宽度
    }
    @Override
    public int getScrollbarPositionX() {
        return this.right - 6;  // 滚动条紧贴右侧
    }
//    @Override





    public void addWaveformEntry(Entry entry) {
        this.addEntry(entry);
    }

    public static class Entry extends ElementListWidget.Entry<Entry> {
        MinecraftClient client = MinecraftClient.getInstance();
        private final TextFieldWidget waveformDataText; //文字输入框
        private final ButtonWidget copyButton, pasteButton, testButton, customButton;          //按钮
        private final TextRenderer textRenderer;        //文本渲染参数
        private final Text text;                        //文本

        private Waveform waveform = new Waveform();

        public Entry(TextRenderer textRenderer, Text text, String key) {
            //设置单个项目相关内容


            if(waveformMap.containsKey(key)) waveform = waveformMap.get(key);


            waveformDataText = new TextFieldWidget(textRenderer,0, 0, 100, ButtonHeight - 4, Text.of(""));
            waveformDataText.setMaxLength(100000);

            waveformDataText.setText(waveform.getWaveform());



//            waveformDataText.setPlaceholder(Text.of("输入波形代码").styled(style -> style.withColor(TextColor.fromRgb(0xaaaaaa))));

            waveformDataText.setChangedListener(inputText -> {
                waveform.setWaveform(inputText);
            });

            customButton = new ButtonWidget.Builder(Text.of("✏"), button -> {
                Screen customScreen = new CustomScreen(key);
                client.setScreen(customScreen);
            }).tooltip(Text.of("点击修改波形")).build();

            copyButton = new ButtonWidget.Builder(Text.of("\uD83D\uDCC4"), button -> {
                MinecraftClient.getInstance().keyboard.setClipboard(waveformDataText.getText());
            }).tooltip(Text.of("点击复制波形代码")).build();

            pasteButton = new ButtonWidget.Builder(Text.of("\uD83D\uDCCB"), button -> {
                String clipboardText = MinecraftClient.getInstance().keyboard.getClipboard();
                waveformDataText.setText(clipboardText);
            }).tooltip(Text.of("点击粘贴波形代码")).build();

            testButton = new ButtonWidget.Builder(Text.of("\uD83D\uDCE8"), button -> {
                webSocketServer.sendDGWaveForm(waveformDataText.getText(), 1);
            }).tooltip(Text.of("发送到终端1通道")).build();


            this.textRenderer = textRenderer;
            this.text = text;

        }


        //确保点击/交互被正确传递
        @Override
        public List<? extends Selectable> selectableChildren() {
            return List.of(waveformDataText, testButton, customButton);
        }

        @Override
        public List<? extends Element> children() {
            return List.of(waveformDataText, testButton, customButton);
        }


        @Override
        public void render(MatrixStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta){
            //渲染相关
            //文本框位置宽高


//            waveformDataText.setDimensionsAndPosition(x + (int) (entryWidth * 0.3), 20, x + (int) (entryWidth * 0.4), y);
            waveformDataText.setPosition(x + (int) (entryWidth * 0.4), y + 2);
            waveformDataText.setWidth(x + (int) (entryWidth * 0.3));
            waveformDataText.render(matrices, mouseX, mouseY, tickDelta);

//            customButton.setDimensionsAndPosition(15, 20, waveformDataText.getX() + waveformDataText.getWidth(), y);
            customButton.setPosition(waveformDataText.getX() + waveformDataText.getWidth() + 2, y);
            customButton.setWidth(15);
            customButton.render(matrices, mouseX, mouseY, tickDelta);

//            copyButton.setDimensionsAndPosition(15, 20, customButton.getX() + 15, y);
//            copyButton.render(context, mouseX, mouseY, tickDelta);
//
//
//            pasteButton.setDimensionsAndPosition(15, 20, copyButton.getX() + 15, y);
//            pasteButton.render(context, mouseX, mouseY, tickDelta);

//            testButton.setDimensionsAndPosition(15, 20, customButton.getX() + 15, y);
            testButton.setPosition(customButton.getX() + 15, y);
            testButton.setWidth(15);
            testButton.render(matrices, mouseX, mouseY, tickDelta);


            drawTextWithShadow(matrices, textRenderer, this.text, x + (int) (entryWidth * 0.15), y + 5, 0xffffff);

            int duration = DGWaveformTool.checkAndCountValidSubstrings(waveformDataText.getText());
            if(duration == 0)
                drawTextWithShadow(matrices, textRenderer, Text.of("ERROR"), testButton.getX() + 20, y + 5, 0xFF0000);
            else drawTextWithShadow(matrices, textRenderer, Text.of((duration * 100) + "ms"), testButton.getX() + 15, y + 5, 0xFFFFFF);

        }



    }

}
