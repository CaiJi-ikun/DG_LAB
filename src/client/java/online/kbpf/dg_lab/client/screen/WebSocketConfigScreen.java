package online.kbpf.dg_lab.client.screen;


import online.kbpf.dg_lab.client.Dg_labClient;
import online.kbpf.dg_lab.client.createQR.ToolQR;
import online.kbpf.dg_lab.client.Config.ModConfig;
import online.kbpf.dg_lab.client.entity.NetworkAdapter;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

@Environment(EnvType.CLIENT)
public class WebSocketConfigScreen extends Screen {

    protected WebSocketConfigScreen() {
        super(Text.literal("连接配置界面"));
    }

    public ModConfig modConfig = Dg_labClient.getModConfig();
    public ButtonWidget autoStartWebSocketServer;
    public ButtonWidget createQR;
    public TextFieldWidget host;
    public TextFieldWidget port;
    public TextFieldWidget serverPort;
    public ButtonWidget host1;
    public ButtonWidget host2;
    public ButtonWidget port1;
    public ButtonWidget serverPort1;
    private NetworkAdapter network = new NetworkAdapter();
    private LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<>(network.getNetworkMap());

    @Override
    public void close() {
        Screen configScreen = new ConfigScreen();
        client.setScreen(configScreen);

    }

    @Override
    protected void init() {
        modConfig = Dg_labClient.getModConfig();
        autoStartWebSocketServer = ButtonWidget.builder(Text.literal("自动启动连接服务器:已" + ((modConfig.getAutoStartWebSocketServer()) ? "开启" : "关闭")), button -> {
            if (modConfig.getAutoStartWebSocketServer()) {
                modConfig.setAutoStartWebSocketServer(false);
                autoStartWebSocketServer.setMessage(Text.literal("自动启动连接服务器:已关闭"));
            } else {
                modConfig.setAutoStartWebSocketServer(true);
                autoStartWebSocketServer.setMessage(Text.literal("自动启动连接服务器:已开启"));

            }

        }).dimensions(width / 2 - (int) (width * 0.41), 20, (int) (width * 0.4), 15).tooltip(Tooltip.of(Text.literal("要在客户端启动时自动启动连接服务器\n如果关闭需要使用指令手动启动\n非必要无需关闭"))).build();

        createQR = ButtonWidget.builder(Text.literal("创建连接二维码并打开"), button -> {
            ToolQR.CreateQR();
        }).dimensions(width / 2 + 5, 20, (int) (width * 0.4), 15).tooltip(Tooltip.of(Text.literal("图片默认生成于此地址:\n" + System.getProperty("user.dir")))).build();

        host = new TextFieldWidget(this.textRenderer, (int) (width * 0.66), 45, (int) (width * 0.25), 15, Text.literal("Enter address..."));
        host.setText(modConfig.getAddress());
        host.setPlaceholder(Text.literal("this").withColor(0xaaaaaa));
        host.setChangedListener(this::hostText);
        host1 = ButtonWidget.builder(Text.literal("?"), button -> {
        }).dimensions((int) (width * 0.63), 45, (int) (width * 0.03), 15).tooltip(Tooltip.of(Text.literal("扫描二维码连接的地址\n非必要无需修改"))).build();
        host2 = ButtonWidget.builder(Text.literal("<|>"), button -> {
            toggleNetworkAdapter();
        }).dimensions((int) (width * 0.59), 45, (int) (width * 0.04), 15).tooltip(Tooltip.of(Text.literal("切换网卡"))).build();

        port = new TextFieldWidget(this.textRenderer, (int) (width * 0.66), 70, (int) (width * 0.25), 15, Text.literal("Enter port..."));
        port.setText(String.valueOf(modConfig.getPort()));
        port.setPlaceholder(Text.literal("9999").withColor(0xaaaaaa));
        port.setChangedListener(this::portText);
        port.setMaxLength(5);
        port1 = ButtonWidget.builder(Text.literal("?"), button -> {
        }).dimensions((int) (width * 0.63), 70, (int) (width * 0.03), 15).tooltip(Tooltip.of(Text.literal("扫描二维码连接的端口,非服务器端口\n非必要无需修改"))).build();

        serverPort = new TextFieldWidget(this.textRenderer, (int) (width * 0.66), 95, (int) (width * 0.25), 15, Text.literal("Enter port..."));
        serverPort.setText(String.valueOf(modConfig.getPort()));
        serverPort.setPlaceholder(Text.literal("9999").withColor(0xaaaaaa));
        serverPort.setChangedListener(this::serverPortText);
        serverPort.setMaxLength(5);
        serverPort1 = ButtonWidget.builder(Text.literal("?"), button -> {
        }).dimensions((int) (width * 0.63), 95, (int) (width * 0.03), 15).tooltip(Tooltip.of(Text.literal("服务器对外开放的端口\n非必要无需修改\n修改后请保存重启客户端生效"))).build();


        addDrawableChild(createQR);
        addDrawableChild(autoStartWebSocketServer);
        addDrawableChild(host);
        addDrawable(host1);
        addDrawableChild(host2);
        addDrawableChild(port);
        addDrawable(port1);
        addDrawableChild(serverPort);
        addDrawable(serverPort1);
    }


    private Timer timer = new Timer(); // 定义一个计时器

    private void hostText(String Text) {

        // 重置计时器
        if (timer != null) {
            timer.cancel();
        }

        // 启动一个新的计时器，延迟更新
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // 停止输入一段时间后的操作
                modConfig.setAddress(Text);
            }
        }, 1000); // 延迟时间为 1000ms

    }

    private void toggleNetworkAdapter(){
        boolean isKeyFound = false;
        for (Map.Entry<String, String> entry : linkedHashMap.entrySet()){
            if(entry.getKey().equals(modConfig.getNetwork())) isKeyFound = true;
            else if(isKeyFound){
                modConfig.setAddress(entry.getValue());
                modConfig.setNetwork(entry.getKey());
                host.setText(entry.getValue());
                return;
            }
        }
        Map.Entry<String, String> firstEntry = linkedHashMap.entrySet().iterator().next();
        modConfig.setAddress(firstEntry.getValue());
        modConfig.setNetwork(firstEntry.getKey());
        host.setText(firstEntry.getValue());
    }

    private void portText(String port) {
        int number;
        try {
            number = Integer.parseInt(port);
            number = (number > 65535 || number < 0) ? 9999 : number;

        } catch (NumberFormatException e) {
            number = 9999;
        }
        modConfig.setPort(number);


    }




    private void serverPortText(String serverPort) {
        int number;
        try {
            number = Integer.parseInt(serverPort);
            number = (number > 65535 || number < 0) ? 9999 : number;

        } catch (NumberFormatException e) {
            number = 9999;
        }
        modConfig.setServerPort(number);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);


        context.drawTextWithShadow(textRenderer, Text.literal("二维码连接的地址"), (int) (width * 0.1), 49, 0xffffff);
        context.drawText(textRenderer, Text.literal(modConfig.getNetwork()), (int) (width * 0.1), 61, 0xaaaaaa, false);
        context.drawTextWithShadow(textRenderer, Text.literal("二维码连接的端口"), (int) (width * 0.1), 74, 0xffffff);
        context.drawTextWithShadow(textRenderer, Text.literal("服务器开放的端口"), (int) (width * 0.1), 99, 0xffffff);




    }

}
