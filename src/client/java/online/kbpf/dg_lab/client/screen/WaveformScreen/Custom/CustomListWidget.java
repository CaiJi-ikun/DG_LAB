package online.kbpf.dg_lab.client.screen.WaveformScreen.Custom;

import net.minecraft.client.MinecraftClient;
//import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
//import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import online.kbpf.dg_lab.client.entity.Waveform.ControlBar;
import online.kbpf.dg_lab.client.screen.ButtonWidget;

import static online.kbpf.dg_lab.client.screen.WaveformScreen.Custom.CustomScreen.list;




import java.util.List;

public class CustomListWidget extends ElementListWidget<CustomListWidget.Entry> {



    public CustomListWidget(MinecraftClient minecraftClient, int width, int height, int top, int bottom, int itemHeight) {
        super(minecraftClient, width, height, top, bottom, itemHeight);
        this.width = width;
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
//    protected int getScrollbarX() {
//        return this.getRight() - 6; // 滚动条紧贴右侧
//    }





    public void addCustomEntry(Entry entry) {
        this.addEntry(entry);
    }

    // 根据索引删除 Entry
    public void removeCustomEntry(int index) {
        List<Entry> children = this.children();
        if (index >= 0 && index < children.size()) {
            this.removeEntry(children.get(index));
        }
    }

    public void removeLast(){
        this.remove(children().size() - 1);
    }


    public static class Entry extends ElementListWidget.Entry<Entry> {

        final Text manual = Text.literal("手动").styled(style -> style.withBold(true).withUnderline(true)), automatic = Text.literal("平均").styled(style -> style.withColor(TextColor.fromRgb(0xAAAAAA)).withBold(true));



        ButtonWidget S_enable, F_enable;
        CustomSliderWidget strength, frequency;
        ControlBar controlBar;
        int index;


        public Entry (int index){
            this.index = index;
            this.controlBar = list.get(this.index);


            S_enable = ButtonWidget.builder((list.get(this.index).isS_on_off())? manual : automatic, button -> {
                this.controlBar.setS_on_off(!this.controlBar.isS_on_off());
                S_enable.setMessage((this.controlBar.isS_on_off()) ? manual : automatic);
                list.set(this.index, this.controlBar);
                if(!controlBar.isS_on_off())
                    updateStrength(getBackStrengthOff(Entry.this.index), getNextStrengthOff(Entry.this.index));
            }).size(22, 8).build();

            F_enable = ButtonWidget.builder((list.get(this.index).isF_on_off())? manual : automatic, button -> {
                this.controlBar.setF_on_off(!this.controlBar.isF_on_off());
                F_enable.setMessage((this.controlBar.isF_on_off()) ? manual : automatic);
                list.set(this.index, this.controlBar);
                if(!controlBar.isF_on_off())
                    updateFrequency(getBackFrequencyOff(Entry.this.index), getNextFrequencyOff(Entry.this.index));
            }).size(22, 8).build();

            strength = new CustomSliderWidget(0, 0, 100 ,8, Text.literal(String.valueOf(list.get(this.index).getStrength())), list.get(this.index).getStrength() * 0.01) {

                @Override
                protected void updateMessage() {
                    Entry.this.controlBar.setStrength((int) (value * 100));
                    list.set(Entry.this.index, Entry.this.controlBar);
                    updateStrength(getBackStrengthOff(Entry.this.index), Entry.this.index);
                    updateStrength(Entry.this.index, getNextStrengthOff(Entry.this.index));
                }

                @Override
                protected void applyValue() {}
            };

            frequency = new CustomSliderWidget(0, 0, 100, 8, Text.literal(String.valueOf(list.get(this.index).getFrequency())), list.get(this.index).getFrequency() * 0.01) {

                @Override
                protected void updateMessage() {


                }

                @Override
                protected void applyValue() {
                    if(value < 0.1) value = 0.1;
                    Entry.this.controlBar.setFrequency((int) (value * 100));
                    list.set(Entry.this.index, Entry.this.controlBar);
                    updateFrequency(getBackFrequencyOff(Entry.this.index), Entry.this.index);
                    updateFrequency(Entry.this.index, getNextFrequencyOff(Entry.this.index));
                }
            };

            updateStrength(getBackStrengthOff(Entry.this.index), Entry.this.index);
            updateStrength(Entry.this.index, getNextStrengthOff(Entry.this.index));
            updateFrequency(getBackFrequencyOff(Entry.this.index), Entry.this.index);
            updateFrequency(Entry.this.index, getNextFrequencyOff(Entry.this.index));

        }

        private void updateFrequency(int indexMin, int indexMax){
            int min = list.get(indexMin).getFrequency();
            double average = (double) (list.get(indexMax).getFrequency() - list.get(indexMin).getFrequency()) / (indexMax - indexMin);
            for(int j = indexMin + 1; j < indexMax; j++){
                ControlBar tmp = list.get(j);
                tmp.setFrequency((int) (min + (average * (j - indexMin))));
                list.set(j, tmp);

            }
        }

        private int getBackFrequencyOff(int index){
            int i = index;
            while (true){
                i--;
                if(i <= 0) {
                    if(i == -1)
                        i = 0;
                    break;
                }
                if(list.get(i).isF_on_off()) break;
            }
            return i;
        }

        private int getNextFrequencyOff(int index){
            int i = index;
            while (true){
                i++;
                if(i >= list.size()) {
                    if(i == list.size())
                        i = list.size() - 1;
                    break;
                }
                if(list.get(i).isF_on_off()) break;
            }
            return i;
        }

        private void updateStrength(int indexMin, int indexMax){
            int min = list.get(indexMin).getStrength();
            double average = (double) (list.get(indexMax).getStrength() - list.get(indexMin).getStrength()) / (indexMax - indexMin);
            for(int j = indexMin + 1; j < indexMax; j++){
                ControlBar tmp = list.get(j);
                tmp.setStrength((int) (min + (average * (j - indexMin))));
                list.set(j, tmp);

            }
        }

        private int getBackStrengthOff(int index){
            int i = index;
            while (true){
                i--;
                if(i <= 0) {
                    if(i == -1)
                        i = 0;
                    break;
                }
                if(list.get(i).isS_on_off()) break;
            }
            return i;
        }

        private int getNextStrengthOff(int index){
            int i = index;
            while (true){
                i++;
                if(i >= list.size()) {
                    if(i == list.size())
                        i = list.size() - 1;
                    break;
                }
                if(list.get(i).isS_on_off()) break;
            }
            return i;
        }



        @Override
        public List<? extends Selectable> selectableChildren() {
            if(!list.get(index).isS_on_off() && !list.get(index).isF_on_off()) return List.of(S_enable, F_enable);
            if(!list.get(index).isF_on_off()) return List.of(S_enable, F_enable, strength);
            if(!list.get(index).isS_on_off()) return List.of(S_enable, F_enable, frequency);
            return List.of(S_enable, F_enable, strength, frequency);

        }

        @Override
        public List<? extends Element> children() {
            if(!list.get(index).isS_on_off() && !list.get(index).isF_on_off()) return List.of(S_enable, F_enable);
            if(!list.get(index).isF_on_off()) return List.of(S_enable, F_enable, strength);
            if(!list.get(index).isS_on_off()) return List.of(S_enable, F_enable, frequency);
            return List.of(S_enable, F_enable, strength, frequency);


        }

        @Override
        public void render(MatrixStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {


//            F_enable.setDimensionsAndPosition(22, 8, (int) (entryWidth * 0.015), y);
            F_enable.setPosition((int) (entryWidth * 0.015), y);

//            frequency.setDimensionsAndPosition((int) (entryWidth * 0.2), 8, F_enable.getX() + 22, y);
            frequency.setPosition(F_enable.getX() + 22, y);
            frequency.setWidth((int) (entryWidth * 0.2));

//            S_enable.setDimensionsAndPosition(22, 8, frequency.getX() + frequency.getWidth() + 20, y);
            S_enable.setPosition(frequency.getX() + frequency.getWidth() + 20, y);

//            strength.setDimensionsAndPosition((int) (entryWidth * 0.6), 8, S_enable.getX() + 22, y);
            strength.setPosition(S_enable.getX() + 22, y);
            strength.setWidth((int) (entryWidth * 0.6));

            if(list.get(this.index) != null) {
                strength.setValue(list.get(this.index).getStrength());
                frequency.setValue(list.get(this.index).getFrequency());
            }


            F_enable.render(matrices, mouseX, mouseY, tickDelta);
            frequency.render(matrices, mouseX, mouseY, tickDelta);
            S_enable.render(matrices, mouseX, mouseY, tickDelta);
            strength.render(matrices, mouseX, mouseY, tickDelta);
        }
    }





}
