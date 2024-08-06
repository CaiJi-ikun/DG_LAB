package buzz.kbpf.dg_lab.client.screen;


import buzz.kbpf.dg_lab.client.Dg_labClient;
import buzz.kbpf.dg_lab.client.createQR.ToolQR;
import buzz.kbpf.dg_lab.client.entity.ModConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

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
    public ButtonWidget port1;
    public ButtonWidget serverPort1;

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

        }).dimensions(width / 2 - 205, 20, 200, 15).tooltip(Tooltip.of(Text.literal("要在客户端启动时自动启动连接服务器\n如果关闭需要使用指令手动启动\n非必要无需关闭"))).build();

        createQR = ButtonWidget.builder(Text.literal("创建连接二维码并打开"), button -> {
            ToolQR.CreateQR();
        }).dimensions(width / 2 + 5, 20, 200, 15).tooltip(Tooltip.of(Text.literal("图片默认生成于此地址:\n" + System.getProperty("user.dir")))).build();

        host = new TextFieldWidget(this.textRenderer, width / 2 + 35, 45, 170, 15, Text.literal("Enter text..."));
        host.setText(modConfig.getHost());
        host.setPlaceholder(Text.literal("this").setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
        host.setChangedListener(this::hostText);
        host1 = ButtonWidget.builder(Text.literal("?"), button -> {
        }).dimensions(width / 2 + 25, 45, 10, 15).tooltip(Tooltip.of(Text.literal("扫描二维码连接的地址\n非必要无需修改\n设置为this自动选择当前局域网地址"))).build();

        port = new TextFieldWidget(this.textRenderer, width / 2 + 35, 70, 170, 15, Text.literal("Enter text..."));
        port.setText(String.valueOf(modConfig.getPort()));
        port.setPlaceholder(Text.literal("9999").setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
        port.setChangedListener(this::portText);
        port.setMaxLength(5);
        port1 = ButtonWidget.builder(Text.literal("?"), button -> {
        }).dimensions(width / 2 + 25, 70, 10, 15).tooltip(Tooltip.of(Text.literal("扫描二维码连接的端口,非服务器端口\n非必要无需修改"))).build();

        serverPort = new TextFieldWidget(this.textRenderer, width / 2 + 35, 95, 170, 15, Text.literal("Enter text..."));
        serverPort.setText(String.valueOf(modConfig.getPort()));
        serverPort.setPlaceholder(Text.literal("9999").setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
        serverPort.setChangedListener(this::serverPortText);
        serverPort.setMaxLength(5);
        serverPort1 = ButtonWidget.builder(Text.literal("?"), button -> {
        }).dimensions(width / 2 + 25, 95, 10, 15).tooltip(Tooltip.of(Text.literal("服务器对外开放的端口\n非必要无需修改\n修改后请保存重启客户端生效"))).build();


        addDrawableChild(createQR);
        addDrawableChild(autoStartWebSocketServer);
        addDrawableChild(host);
        addDrawable(host1);
        addDrawableChild(port);
        addDrawable(port1);
        addDrawableChild(serverPort);
        addDrawable(serverPort1);
    }


    public void hostText(String Text) {
        modConfig.setHost(Text);
    }

    public void portText(String port) {
        int number;
        try {
            number = Integer.parseInt(port);
            number = (number > 65535 || number < 0) ? 9999 : number;

        } catch (NumberFormatException e) {
            number = 9999;
        }
        modConfig.setPort(number);


    }

    public void serverPortText(String serverPort) {
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


        context.drawTextWithShadow(textRenderer, Text.literal("二维码连接的地址"), width / 2 - 190, 48, 0xffffff);
        context.drawTextWithShadow(textRenderer, Text.literal("二维码连接的端口"), width / 2 - 190, 73, 0xffffff);
        context.drawTextWithShadow(textRenderer, Text.literal("服务器开放的端口"), width / 2 - 190, 98, 0xffffff);
    }

}
