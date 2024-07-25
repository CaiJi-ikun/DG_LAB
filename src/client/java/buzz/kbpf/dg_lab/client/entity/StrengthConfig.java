package buzz.kbpf.dg_lab.client.entity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.io.*;

public class StrengthConfig {

    private int ADownTime, BDownTime, ADownValue, BDownValue, ADelayTime, BDelayTime;
    private float ADamageStrength, BDamageStrength;


    public void savaFile(){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try {
            File file = new File("config/dg-lab/StrengthConfig.json");
            if (!file.exists()) {
                file.getParentFile().mkdirs(); // 创建父目录
                file.createNewFile(); // 创建文件
            }
            try (Writer writer = new FileWriter(file)) {
                gson.toJson(this, writer);
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Handle IOException
        }
    }

    public static StrengthConfig loadJson() {
        Gson gson = new Gson();
        File file = new File("config/dg-lab/StrengthConfig.json");
        if (!file.exists()) {
            return new StrengthConfig(3, 3, 5, 5, 1, 1, 50, 50); // 默认的对象，可以根据需求初始化
        }
        try (Reader reader = new FileReader("config/dg-lab/config.json")) {
            return gson.fromJson(reader, StrengthConfig.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            // Handle FileNotFoundException
        } catch (JsonSyntaxException | IOException e) {
            e.printStackTrace();
            // Handle other JSON related exceptions
        }
        return null;
    }





    public StrengthConfig() {
    }

    public StrengthConfig(int ADamageStrength, int BDamageStrength, int ADownTime, int BDownTime, int ADownValue, int BDownValue, int ADelayTime, int BDelayTime) {
        this.ADamageStrength = ADamageStrength;
        this.BDamageStrength = BDamageStrength;
        this.ADownTime = ADownTime;
        this.BDownTime = BDownTime;
        this.ADownValue = ADownValue;
        this.BDownValue = BDownValue;
        this.ADelayTime = ADelayTime;
        this.BDelayTime = BDelayTime;
    }


    public int getADownValue() {
        return ADownValue;
    }

    public void setADownValue(int ADownValue) {
        this.ADownValue = ADownValue;
    }

    public int getBDownValue() {
        return BDownValue;
    }

    public void setBDownValue(int BDownValue) {
        this.BDownValue = BDownValue;
    }

    public int getADelayTime() {
        return ADelayTime;
    }

    public void setADelayTime(int ADelayTime) {
        this.ADelayTime = ADelayTime;
    }

    public int getBDelayTime() {
        return BDelayTime;
    }

    public void setBDelayTime(int BDelayTime) {
        this.BDelayTime = BDelayTime;
    }

    public float getADamageStrength() {
        return ADamageStrength;
    }

    public void setADamageStrength(float ADamageStrength) {
        this.ADamageStrength = ADamageStrength;
    }

    public float getBDamageStrength() {
        return BDamageStrength;
    }

    public void setBDamageStrength(float BDamageStrength) {
        this.BDamageStrength = BDamageStrength;
    }

    public int getADownTime() {
        return ADownTime;
    }

    public void setADownTime(int ADownTime) {
        this.ADownTime = ADownTime;
    }

    public int getBDownTime() {
        return BDownTime;
    }

    public void setBDownTime(int BDownTime) {
        this.BDownTime = BDownTime;
    }
}