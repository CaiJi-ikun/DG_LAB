package buzz.kbpf.dg_lab.client.entity;

public class DGStrength {
    private int AStrength, BStrength, AMaxStrength, BMaxStrength, ADelayTime, BDelayTime;

    public DGStrength() {
        AStrength = 0;
        AMaxStrength = 0;
        BStrength = 0;
        BMaxStrength = 0;
    }

    public DGStrength(int AStrength, int BStrength, int AMaxStrength, int BMaxStrength) {
        this.AStrength = AStrength;
        this.BStrength = BStrength;
        this.AMaxStrength = AMaxStrength;
        this.BMaxStrength = BMaxStrength;
    }

    public int getADelayTime() {
        return ADelayTime;
    }

    public void setADelayTime(int ADelayTime) {
        this.ADelayTime = Math.max(ADelayTime, 0);
    }

    public int getBDelayTime() {
        return BDelayTime;
    }

    public void setBDelayTime(int BDelayTime) {
        this.BDelayTime = Math.max(BDelayTime, 0);
    }

    public int getAStrength() {
        return AStrength;
    }

    public void setAStrength(int AStrength) {
        this.AStrength = Math.max(AStrength, 0);
    }

    public int getBStrength() {
        return BStrength;
    }

    public void setBStrength(int BStrength) {
        this.BStrength = Math.max(BStrength, 0);
    }

    public int getAMaxStrength() {
        return AMaxStrength;
    }

    public void setAMaxStrength(int AMaxStrength) {
        this.AMaxStrength = AMaxStrength;
    }

    public int getBMaxStrength() {
        return BMaxStrength;
    }

    public void setBMaxStrength(int BMaxStrength) {
        this.BMaxStrength = BMaxStrength;
    }
}
