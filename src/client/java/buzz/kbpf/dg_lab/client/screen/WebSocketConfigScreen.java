package buzz.kbpf.dg_lab.client.screen;


import buzz.kbpf.dg_lab.client.Dg_labClient;
import buzz.kbpf.dg_lab.client.createQR.ToolQR;
import buzz.kbpf.dg_lab.client.entity.ModConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
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

    @Override
    public void close(){
            Screen configScreen = new ConfigScreen();
            client.setScreen(configScreen);

    }

    @Override
    protected void init(){
        modConfig = Dg_labClient.getModConfig();
        autoStartWebSocketServer = ButtonWidget.builder(Text.literal("自动启动连接服务器:已" + ((modConfig.getAutoStartWebSocketServer()) ? "开启" : "关闭")), button -> {
            if(modConfig.getAutoStartWebSocketServer()){
                modConfig.setAutoStartWebSocketServer(false);
                autoStartWebSocketServer.setMessage(Text.literal("自动启动连接服务器:已关闭"));
            }else {
                modConfig.setAutoStartWebSocketServer(true);
                autoStartWebSocketServer.setMessage(Text.literal("自动启动连接服务器:已开启"));

            }

        }).dimensions(width / 2 - 205, 20, 200, 20).tooltip(Tooltip.of(Text.literal("要在客户端启动时自动启动连接服务器\n如果关闭需要使用指令手动启动"))).build();

        createQR = ButtonWidget.builder(Text.literal("创建连接二维码并打开"), button -> {
            ToolQR.CreateQR();
        }).dimensions(width / 2 + 5, 20, 200, 20).tooltip(Tooltip.of(Text.literal("图片默认生成于此地址:\n" + System.getProperty("user.dir")))).build();

        host = new TextFieldWidget(this.textRenderer, width / 2 + 35, 50, 180, 20, Text.literal("Enter text..."));
        host.setText(modConfig.getHost());
        host.setPlaceholder(Text.literal("二维码连接地址").setStyle(Style.EMPTY.withColor(Formatting.GRAY)));

        addDrawableChild(host);
        addDrawableChild(autoStartWebSocketServer);
        addDrawableChild(createQR);
    }


}
