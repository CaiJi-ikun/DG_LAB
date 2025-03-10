package online.kbpf.dg_lab.client.screen.WaveformScreen.Custom;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;
import online.kbpf.dg_lab.client.entity.Waveform.ControlBar;

import static online.kbpf.dg_lab.client.screen.WaveformScreen.Custom.CustomScreen.list;




import java.util.List;

public class CustomListWidget extends ElementListWidget<CustomListWidget.Entry> {



    public CustomListWidget(MinecraftClient minecraftClient, int width, int height, int y, int itemHeight) {
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



        ButtonWidget S_enable, F_enable;
        SliderWidget strength, frequency;
        ControlBar controlBar;
        int index;


        public Entry (int index){
            this.index = index;
            this.controlBar = new ControlBar();
            this.controlBar = list.get(this.index);


            S_enable = ButtonWidget.builder(Text.of((list.get(this.index).isS_on_off())? "手动" : "平均"), button -> {
                this.controlBar.setS_on_off(!this.controlBar.isS_on_off());
                S_enable.setMessage(Text.of((this.controlBar.isS_on_off()) ? "手动" : "平均"));
                list.set(this.index, this.controlBar);
            }).build();

            F_enable = ButtonWidget.builder(Text.of((list.get(this.index).isF_on_off())? "手动" : "平均"), button -> {
                this.controlBar.setF_on_off(!this.controlBar.isF_on_off());
                F_enable.setMessage(Text.of((this.controlBar.isF_on_off()) ? "手动" : "平均"));
                list.set(this.index, this.controlBar);
            }).build();

            strength = new SliderWidget(0, 0, 100 ,15, Text.literal(String.valueOf(list.get(this.index).getStrength())), list.get(this.index).getStrength() * 0.01) {
                @Override
                protected void updateMessage() {
                    strength.setMessage(Text.literal(String.valueOf((int) (value * 100))));
                }

                @Override
                protected void applyValue() {

//                    if(list.get(Entry.this.index).isS_on_off()){
//                        int i = Entry.this.index;
//                        double average;
//                        while (true){
//                            i--;
//                            if(i <= 0) {
//                                if(i == -1)
//                                    i++;
//                                break;
//                            }
//                            if(list.get(i).isS_on_off()) break;
//                        }
//
//                        ControlBar min = list.get(i);
//                        average = (double) (list.get(Entry.this.index).getStrength() - min.getStrength()) / (Entry.this.index - i);
//                        for(int j = i + 1; j < Entry.this.index; j++){
//                            ControlBar tmp = min;
//                            tmp.setStrength((int) (tmp.getStrength() + (average * j - i)));
//                            list.set(j, tmp);
//                        }
//
//
//                        i = Entry.this.index;
//                        while (true){
//                            i++;
//                            if(i >= list.size() - 1) {
//                                if(i == list.size())
//                                    i--;
//                                break;
//                            }
//                            if(list.get(i).isS_on_off()) break;
//                        }
//
//                        min = list.get(Entry.this.index);
//                        average = (double) (min.getStrength() - list.get(Entry.this.index).getStrength()) / (i - Entry.this.index);
//                        for(int j = Entry.this.index + 1; j < i; j++){
//                            ControlBar tmp = min;
//                            tmp.setStrength((int) (tmp.getStrength() + (average * j - i)));
//                            list.set(j, tmp);
//                        }
//
//                    }




                    Entry.this.controlBar.setStrength((int) (value * 100));
                    list.set(Entry.this.index, Entry.this.controlBar);

                }
            };

            frequency = new SliderWidget(0, 0, 100, 15, Text.literal(String.valueOf(list.get(this.index).getFrequency())), list.get(this.index).getFrequency() * 0.01) {
                @Override
                protected void updateMessage() {
                    if(value < 0.1) value = 0.1;
                    frequency.setMessage(Text.literal(String.valueOf((int) (value * 100))));
                }

                @Override
                protected void applyValue() {
                    Entry.this.controlBar.setFrequency((int) (value * 100));
                    list.set(Entry.this.index, Entry.this.controlBar);
                }
            };
        }

        @Override
        public List<? extends Selectable> selectableChildren() {
            if(!list.get(index).isS_on_off() && !list.get(index).isF_on_off()) return List.of(S_enable, F_enable);
            if(!list.get(index).isF_on_off()) return List.of(S_enable, F_enable, strength);
            if(!list.get(index).isS_on_off()) return List.of(S_enable, F_enable, frequency);
            return List.of(S_enable, S_enable, strength, frequency);

        }

        @Override
        public List<? extends Element> children() {
            if(!list.get(index).isS_on_off() && !list.get(index).isF_on_off()) return List.of(S_enable, F_enable);
            if(!list.get(index).isF_on_off()) return List.of(S_enable, F_enable, strength);
            if(!list.get(index).isS_on_off()) return List.of(S_enable, F_enable, frequency);
            return List.of(S_enable, S_enable, strength, frequency);


        }

        @Override
        public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            F_enable.setDimensionsAndPosition(22, 15, (int) (entryWidth * 0.05), y);
            frequency.setDimensionsAndPosition((int) (entryWidth * 0.2), 15, F_enable.getX() + 22, y);
            S_enable.setDimensionsAndPosition(22, 15, frequency.getX() + frequency.getWidth() + 5, y);
            strength.setDimensionsAndPosition((int) (entryWidth * 0.4), 15, S_enable.getX() + 22, y);

            F_enable.render(context, mouseX, mouseY, tickDelta);
            frequency.render(context, mouseX, mouseY, tickDelta);
            S_enable.render(context, mouseX, mouseY, tickDelta);
            strength.render(context, mouseX, mouseY, tickDelta);
        }
    }





}
