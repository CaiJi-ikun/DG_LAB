package online.kbpf.dg_lab.client.entity.Waveform;



import online.kbpf.dg_lab.client.Tool.DGWaveformTool;

import java.util.ArrayList;
import java.util.List;

import static online.kbpf.dg_lab.client.Tool.DGWaveformTool.checkAndCountValidSubstrings;

public class Waveform {

    private String waveform, name = "empty";                        //存储波形字符串和当前波形名字
    private int duration = 0;                                       //波形时间长度
    private List<ControlBar> list = new ArrayList<>();              //ui编辑的数据


    public Waveform() {
        List<ControlBar> list = new ArrayList<>();
        ControlBar controlBar = new ControlBar(0, 0, false, false);
        for(int i = 1; i <= 4; i++)
            list.add(controlBar);
    }

    public Waveform(List<ControlBar> list) {
        this.list = list;
        this.GraphToData();
        updateDuration();
    }

    public Waveform(String waveform) {
        this.waveform = waveform;
        updateDuration();

        list.add(new ControlBar(0, 0, false, false));
        list.add(new ControlBar(0, 0, false, false));
        list.add(new ControlBar(0, 0, false, false));
        list.add(new ControlBar(0, 0, false, false));

    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setWaveform(String waveform) {
        this.waveform = waveform;
        updateDuration();
    }

    public void setList(List<ControlBar> list) {
        this.list = list;
    }

    public String getWaveform() {
        return waveform;
    }

    public int getDuration() {
        return duration;
    }

    public boolean updateDuration() {
        duration = checkAndCountValidSubstrings(waveform);
        return duration <= 0;
    }

    public List<ControlBar> getList() {
        return list;
    }

    public Waveform DataToGraph(){
        //字符串转ui
        if(checkAndCountValidSubstrings(this.waveform) > 0) {//检测字符串合法
            list = new ArrayList<>();
            String[] waveform = this.waveform.split(",");//按照逗号分割

            for (String str : waveform) {
                String string = str.substring(1, str.length() - 1);

                String[] bytes = new String[8]; // 共8个字节
                for (int i = 0; i < 8; i++) {
                    bytes[i] = string.substring(i * 2, (i * 2) + 2);
                }

                for (int i = 0; i < 4; i++) {
                    list.add(new ControlBar(Integer.parseInt(bytes[i + 4], 16), (Integer.parseInt(bytes[i], 16)), true, true));
                }
            }
        }
        return this;
    }

    public void GraphToData(){
        //ui转字符串
        StringBuilder waveform = new StringBuilder(), strength = new StringBuilder(), frequency = new StringBuilder();
        for(int i = 0; i < list.size(); i++){
            frequency.append(String.format("%02x", list.get(i).getFrequency()));
            strength.append(String.format("%02x", list.get(i).getStrength()));
            if((i + 1) % 4 == 0) {
                waveform.append('\"');
                waveform.append(frequency);
                waveform.append(strength);
                waveform.append('\"');
                waveform.append(',');
                frequency = new StringBuilder();
                strength = new StringBuilder();
            }
        }
        waveform.deleteCharAt(waveform.length() - 1);
        this.waveform = waveform.toString();
        updateDuration();
    }


}
