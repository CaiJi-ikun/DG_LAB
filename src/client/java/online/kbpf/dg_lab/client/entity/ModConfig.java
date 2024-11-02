package online.kbpf.dg_lab.client.entity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.io.*;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

public class ModConfig {
    private boolean AutoStartWebSocketServer = true;
    private int RenderingPositionX = 20;
    private int RenderingPositionY = 20;
    private int port = 9999, serverPort = port;
    private String address, network;
    private boolean address2 = false, network2 = false;



    public ModConfig(boolean autoStartWebSocketServer, int x, int y) {
        AutoStartWebSocketServer = autoStartWebSocketServer;
        RenderingPositionX = x;
        RenderingPositionY = y;
        autoGetNetworkAddress();

    }

    public void autoGetNetworkAddress(){
        try {
            InetAddress localhost = InetAddress.getLocalHost();
            if(localhost != null) {
                address = localhost.getHostAddress();
                address2 = true;
                NetworkInterface networkInterface = NetworkInterface.getByInetAddress(localhost);
                if(networkInterface != null) {
                    network = networkInterface.getDisplayName();
                    network2 =true;
                }
            }
        } catch (UnknownHostException | SocketException e) {
            throw new RuntimeException(e);
        }
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

    public String getAddress() {
        if(address2)
            return address;
        else return "error";
    }

    public String getNetwork() {
        if(network2)
            return network;
        else return "unknown";
    }


    public void setNetwork(String network) {
        this.network = network;
    }

    public void setAddress(String address) {
        this.address = address;
        try {
            // 通过IP地址获取InetAddress对象
            InetAddress inetAddress = InetAddress.getByName(address);
            address2 = true;
            network2 = false;
            if(inetAddress != null) {
                // 通过InetAddress获取对应的网卡

                NetworkInterface networkInterface = NetworkInterface.getByInetAddress(inetAddress);
                if(networkInterface != null) {
                    network2 = true;
                    network = networkInterface.getDisplayName();
                }


            }

        } catch (UnknownHostException | SocketException e) {
            e.printStackTrace();
        }
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
            NetworkAdapter networkInterface = new NetworkAdapter();
            ModConfig modConfig = gson.fromJson(reader, ModConfig.class);
            if(!modConfig.address2 || (modConfig.network2&&networkInterface.getNetworkMap().size() == 1)) modConfig.autoGetNetworkAddress();
            else{
                String address = networkInterface.NICGetaddress(modConfig.address);
                if(address != null)
                    modConfig.setAddress(address);
            }
            return modConfig;
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
