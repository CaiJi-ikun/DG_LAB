package buzz.kbpf.dg_lab.client.entity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.io.*;

public class ModConfig {
    private boolean AutoStartWebSocketServer = true;
    private int RenderingPositionX = 20;
    private int RenderingPositionY = 20;
    private int port = 9999, serverPort = port;
    private String host = "this";



    public ModConfig(boolean autoStartWebSocketServer, int x, int y) {
        AutoStartWebSocketServer = autoStartWebSocketServer;
        RenderingPositionX = x;
        RenderingPositionY = y;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = (port <0 || port > 65535) ? 9999 : port;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = (serverPort <0 || serverPort > 65535) ? 9999 : serverPort;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

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



    public boolean getAutoStartWebSocketServer() {
        return AutoStartWebSocketServer;
    }

    public void setAutoStartWebSocketServer(boolean autoStartWebSocketServer) {
        AutoStartWebSocketServer = autoStartWebSocketServer;
    }
}
