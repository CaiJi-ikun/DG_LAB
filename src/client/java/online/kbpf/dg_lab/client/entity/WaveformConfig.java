package online.kbpf.dg_lab.client.entity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.util.Map;

public class WaveformConfig {

    private WaveformConfig(){}

    public static Map<String, String> LoadWave() {
        Gson gson = new Gson();
        File file = new File("config/dg-lab/WaveformData.json");
        //读取文件
        if(file.exists()) {
            try (Reader reader = new FileReader("config/dg-lab/WaveformData.json")) {
                return gson.fromJson(reader, new TypeToken<Map<String, String>>() {
                }.getType());
                //返回数据
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }


    // 保存 Waveform 配置数据到文件
    public static void saveWave(Map<String, String> waveformData) {
        Gson gson = new Gson();
        File file = new File("config/dg-lab/WaveformData.json");

        // 确保文件存在
        if (!file.exists()) {
            file.getParentFile().mkdirs(); // 创建父目录
            try {
                file.createNewFile(); // 创建文件
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        try (Writer writer = new FileWriter(file)) {
            // 将 Map 转换为 JSON 字符串，并写入文件
            gson.toJson(waveformData, writer);
        } catch (IOException e) {
            e.printStackTrace(); // 处理异常
        }
    }

}




