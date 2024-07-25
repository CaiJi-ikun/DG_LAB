package buzz.kbpf.dg_lab.client.entity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.io.*;

public class ModConfig {
    boolean AutoStartWebSocketServer = true;

    public void savaFile(){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try {
            File file = new File("config/dg-lab/ModConfig.json");
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

    public static ModConfig loadJson() {
        Gson gson = new Gson();
        File file = new File("config/dg-lab/ModConfig.json");
        if (!file.exists()) {
            return new ModConfig(true); // 默认的对象，可以根据需求初始化
        }
        try (Reader reader = new FileReader("config/dg-lab/ModConfig.json")) {
            return gson.fromJson(reader, ModConfig.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            // Handle FileNotFoundException
        } catch (JsonSyntaxException | IOException e) {
            e.printStackTrace();
            // Handle other JSON related exceptions
        }
        return null;
    }


    public ModConfig() {
    }

    public ModConfig(boolean autoStartWebSocketServer) {
        AutoStartWebSocketServer = autoStartWebSocketServer;
    }

    public boolean getAutoStartWebSocketServer() {
        return AutoStartWebSocketServer;
    }

    public void setAutoStartWebSocketServer(boolean autoStartWebSocketServer) {
        AutoStartWebSocketServer = autoStartWebSocketServer;
    }
}
