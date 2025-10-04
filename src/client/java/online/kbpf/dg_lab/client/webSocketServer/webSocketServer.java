package online.kbpf.dg_lab.client.webSocketServer;

import net.minecraft.text.LiteralText;
import online.kbpf.dg_lab.client.entity.DGFrequency;
import online.kbpf.dg_lab.client.entity.DGStrength;
import online.kbpf.dg_lab.client.entity.clientInfo;
import com.google.gson.Gson;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import net.minecraft.client.MinecraftClient;
import java.net.InetSocketAddress;
import java.util.Timer;
import java.util.TimerTask;


import static online.kbpf.dg_lab.client.Dg_labClient.waveformMap;


public class webSocketServer extends WebSocketServer {

    // 指示服务器是否正在运行
    private boolean isRunning = false;

    // 指示是否有客户端连接
    private boolean isConnected = false;

    // 与客户端的WebSocket连接
    private WebSocket client;

    // 客户端信息
    private clientInfo clientInfo = new clientInfo("bind", "1234-123456789-12345-12345-00", "", "targetId");

    // 存储强度相关数据的DGStrength对象
    private DGStrength dgStrength = new DGStrength();

    //存储频率相关数据的DGFrequency对象


    /**
     * webSocketServer类的构造函数。
     * @param address 绑定WebSocket服务器的地址。
     */
    public webSocketServer(InetSocketAddress address) {
        super(address);
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        if (!isConnected) {
            dgStrength = new DGStrength();
            isConnected = true;
            client = conn;
            // 在连接时发送客户端信息
            client.send(new Gson().toJson(clientInfo, clientInfo.class));
            MinecraftClient.getInstance().player.sendMessage(Text.of("==="),true);
            MinecraftClient.getInstance().player.sendMessage(Text.of("作者不对使用此模组造成的人身伤害和精神伤害负责"),true);
            MinecraftClient.getInstance().player.sendMessage(Text.of("使用此模组请自行注意人身安全"),true);
            MinecraftClient.getInstance().player.sendMessage(Text.of("==="),true);

            // 创建一个定时器以定期发送心跳和更新强度信息
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {

                @Override
                public void run() {
                    if (isConnected) {
                            clientInfo.setType("heartbeat");
                            clientInfo.setMessage("200");
                            conn.send(new Gson().toJson(clientInfo, clientInfo.class));


                    }
                }
            }, 0, 60000); // 每1min执行一次
        } else {
            conn.send("{\"type\":\"error\",\"message\":\"400\"}");
        }
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        if (conn.equals(client)) {
            isConnected = false;
            client = null;
            clientInfo = new clientInfo("bind", "1234-123456789-12345-12345-00", "", "targetId");
        }
    }

    @Override
    public void onMessage(WebSocket conn, String message) {

        clientInfo tmp = new Gson().fromJson(message, clientInfo.class);

        if (tmp.getMessage().equals("DGLAB") && tmp.getType().equals("bind") && tmp.getClientId().equals("1234-123456789-12345-12345-01") && tmp.getTargetId().equals(clientInfo.getClientId())) {
            clientInfo = tmp;
            clientInfo.setMessage("200");
            client.send(new Gson().toJson(clientInfo, clientInfo.class));
        } else if (tmp.getType().equals("msg")) {
            String message1 = tmp.getMessage();
            StringBuilder number = new StringBuilder();
            int count = 0;
            boolean iscount = false;
            for (char ch : message1.toCharArray()) {
                if (Character.isDigit(ch)) {
                    iscount = true;
                    number.append(ch);
                } else if (iscount) {
                    switch (count) {
                        case 0:
                            dgStrength.setAStrength(Integer.parseInt(number.toString()));
                            break;
                        case 1:
                            dgStrength.setBStrength(Integer.parseInt(number.toString()));
                            break;
                        case 2:
                            dgStrength.setAMaxStrength(Integer.parseInt(number.toString()));
                            break;
                        case 3:
                            dgStrength.setBMaxStrength(Integer.parseInt(number.toString()));
                            break;
                    }
                    number = new StringBuilder();
                    count++;
                }
            }
            int Number = Integer.parseInt(number.toString());
            if (Number == 405) {
                if (MinecraftClient.getInstance().player != null)

                    MinecraftClient.getInstance().player.sendMessage(new LiteralText("发送的消息长度超过1950").setStyle(Style.EMPTY.withColor(0xFF0000)), false);
            }

            // 设置最终的BMaxStrength值
            else dgStrength.setBMaxStrength(Number);
        }
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        ex.printStackTrace(); // 打印错误信息
    }

    @Override
    public void onStart() {
        isRunning = true; // 设置服务器为运行状态
    }

    /**
     * 向连接的客户端发送消息。
     * @param value 强度值
     * @param mode 强度变化模式（0: 减少，1: 增加，2: 设置为指定值）
     * @param A1or2B 与强度相关的通道（1: A通道，2: B通道）
     */
    public void sendStrengthToClient(int value, int mode, int A1or2B) {
        if (isConnected) {
            clientInfo.setMessage("strength-" + A1or2B + '+' + mode + '+' + value);
            clientInfo.setType("msg");
            client.send(new Gson().toJson(clientInfo, clientInfo.class));
        }
    }

    /**
     * 设置A通道和B通道的倒计时时间。
     * @param A A通道的倒计时时间
     * @param B B通道的倒计时时间
     */
    public void setDelayTime(int A, int B) {
        dgStrength.setADelayTime(A);
        dgStrength.setBDelayTime(B);
    }

    /**
     * 设置强度值。
     * @param DGStrength 包含新强度值的DGStrength对象
     */
    public void setStrength(DGStrength DGStrength) {
        dgStrength = DGStrength;
    }

    /**
     * 向连接的客户端发送当前强度值。
     */
    public void sendStrength() {
        if (isConnected) {
            clientInfo.setType("msg");
            clientInfo.setMessage("strength-1+2+" + Math.min(dgStrength.getAStrength(), dgStrength.getAMaxStrength()));
            client.send(new Gson().toJson(clientInfo, clientInfo.class));

            clientInfo.setMessage("strength-2+2+" + Math.min(dgStrength.getBStrength(), dgStrength.getBMaxStrength()));
            client.send(new Gson().toJson(clientInfo, clientInfo.class));
        }
    }

    /**
     * 获取当前的强度值。
     * @return 包含强度值的DGStrength对象
     */
    public DGStrength getStrength() {
        return dgStrength;
    }

    //清除频率
    public void CleanFrequency(int A1orB2) {
        if(isConnected) {
            clientInfo.setType("msg");
            if(A1orB2 == 1) clientInfo.setMessage("clear-1");
            else if(A1orB2 == 2) clientInfo.setMessage("clear-2");
            client.send(new Gson().toJson(clientInfo, online.kbpf.dg_lab.client.entity.clientInfo.class));
        }
    }




    /**
     * 根据指定类型将DG频率数据发送给客户端。
     *
     * @param Damage2orHealth3 要发送的数据类型：
     *                                    1 表示自定义频率数据，
     *                                    2 表示伤害频率数据，
     *                                    3 表示治疗频率数据。
     * @param cleanPrevious 如果为true，则在发送新消息之前清除之前的消息。
     */
    public void sendDgWaveform(int Damage2orHealth3, boolean cleanPrevious, int A1orB2) {

        if (isConnected) {
            // 如果需要清除之前的数据
            if (cleanPrevious) CleanFrequency(A1orB2);


            clientInfo.setType("msg"); // 将消息类型设置为"msg"

            // 检查要发送的数据类型
            if (Damage2orHealth3 == 2) {
                // 发送伤害波形
                if (A1orB2 == 1) {
                    // 发送到A通道
                    clientInfo.setMessage("pulse-A:[" + waveformMap.get("ADamage").getWaveform() + "]");
                    client.send(new Gson().toJson(clientInfo, online.kbpf.dg_lab.client.entity.clientInfo.class));

                } else if (A1orB2 == 2) {
                    // 发送到B通道
                    clientInfo.setMessage("pulse-B:[" + waveformMap.get("BDamage").getWaveform() + "]");
                    client.send(new Gson().toJson(clientInfo, online.kbpf.dg_lab.client.entity.clientInfo.class));
                }
            } else if (Damage2orHealth3 == 3) {
                // 发送治疗波形
                if (A1orB2 == 1) {
                    // 发送到A通道
                    clientInfo.setMessage("pulse-A:[" + waveformMap.get("AHealing").getWaveform() + "]");
                    client.send(new Gson().toJson(clientInfo, online.kbpf.dg_lab.client.entity.clientInfo.class));
                } else if (A1orB2 == 2) {
                    // 发送到B通道
                    clientInfo.setMessage("pulse-B:[" + waveformMap.get("BHealing").getWaveform() + "]");
                    client.send(new Gson().toJson(clientInfo, online.kbpf.dg_lab.client.entity.clientInfo.class));
                }
            }
        }
    }


    public void sendDGWaveForm(String message, int A1orB2){
        if(isConnected){
            clientInfo.setType("msg");
            if(A1orB2 == 1) {
                CleanFrequency(1);
                clientInfo.setMessage("pulse-A:[" + message + "]");
            }
            else {
                CleanFrequency(2);
                clientInfo.setMessage("pulse-B:[" + message + "]");
            }
            client.send(new Gson().toJson(clientInfo, online.kbpf.dg_lab.client.entity.clientInfo.class));
        }
    }





    /**
     * 检查服务器是否正在运行。
     * @return 如果服务器正在运行，则返回true；否则返回false。
     */
    public boolean getState() {
        return isRunning;
    }

    /**
     * 检查是否有客户端当前连接。
     * @return 如果有客户端连接，则返回true；否则返回false。
     */
    public boolean getConnected() {
        return isConnected;
    }
}
