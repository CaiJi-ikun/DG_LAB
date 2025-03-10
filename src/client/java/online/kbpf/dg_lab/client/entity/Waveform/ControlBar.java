package online.kbpf.dg_lab.client.entity.Waveform;

public class ControlBar {



    private int strength, frequency = 10;
    private boolean S_on_off, F_on_off;


    public ControlBar(int strength, int frequency, boolean s_on_off, boolean f_on_off) {
        this.strength = strength;
        this.frequency = (frequency < 10 || frequency > 100) ? 10 : frequency;
        S_on_off = s_on_off;
        F_on_off = f_on_off;
    }

    public ControlBar() {
        S_on_off = false;
        F_on_off = false;
        frequency = 10;
        strength = 0;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = (strength < 0 || strength > 100) ? 0 : strength;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = (frequency < 10 || frequency > 100) ? 10 : frequency;
    }

    public boolean isS_on_off() {
        return S_on_off;
    }

    public void setS_on_off(boolean s_on_off) {
        S_on_off = s_on_off;
    }

    public boolean isF_on_off() {
        return F_on_off;
    }

    public void setF_on_off(boolean f_on_off) {
        F_on_off = f_on_off;
    }


}
