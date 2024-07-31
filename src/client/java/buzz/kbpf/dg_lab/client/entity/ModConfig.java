package buzz.kbpf.dg_lab.client.entity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.io.*;

public class ModConfig {
    boolean AutoStartWebSocketServer = true;
    int RenderingPositionX = 20;
    int RenderingPositionY = 20 ;

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
            return new ModConfig(true, 20, 20); // 默认的对象，可以根据需求初始化
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


    public int getRenderingPositionX() {
        return RenderingPositionX;
    }

    public void setRenderingPositionX(int renderingPositionX) {
        RenderingPositionX = Math.max(0, renderingPositionX);
    }

    public int getRenderingPositionY() {
        return RenderingPositionY;
    }

    public void setRenderingPositionY(int renderingPositionY) {
        RenderingPositionY =Math.max(0, renderingPositionY);
    }

    public ModConfig() {
    }

    public ModConfig(boolean autoStartWebSocketServer, int x, int y) {
        AutoStartWebSocketServer = autoStartWebSocketServer;
        RenderingPositionX = x;
        RenderingPositionY = y;
    }

    public boolean getAutoStartWebSocketServer() {
        return AutoStartWebSocketServer;
    }

    public void setAutoStartWebSocketServer(boolean autoStartWebSocketServer) {
        AutoStartWebSocketServer = autoStartWebSocketServer;
    }
}
