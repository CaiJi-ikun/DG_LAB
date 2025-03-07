package online.kbpf.dg_lab.client;

import online.kbpf.dg_lab.client.command.Default;
import online.kbpf.dg_lab.client.entity.ModConfig;
import online.kbpf.dg_lab.client.entity.StrengthConfig;
import online.kbpf.dg_lab.client.screen.ConfigScreen;
import online.kbpf.dg_lab.client.webSocketServer.webSocketServer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;



public class Dg_labClient implements ClientModInitializer {

    private static webSocketServer webSocketServer = null;
    private static StrengthConfig StrengthConfig = new StrengthConfig();
    private static final ModConfig modConfig = ModConfig.loadJson();
    private static KeyBinding keyBinding;
    private final Screen ConfigScreen = new ConfigScreen();
    public static Map<String, String> waveformDataMap = new HashMap<>();



    @Override
    public void onInitializeClient() {

        webSocketServer = new webSocketServer(new InetSocketAddress(modConfig.getServerPort()));

        StrengthConfig = online.kbpf.dg_lab.client.entity.StrengthConfig.loadJson();
        HudRenderCallback.EVENT.register(this::onHudRender);

        keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "打开配置界面",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_O,
                "DG_LAB"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (keyBinding.wasPressed()) {

                client.setScreen(ConfigScreen);
            }
        });
        //指令定义
        Default.register(modConfig, StrengthConfig, webSocketServer);

        if(modConfig.getAutoStartWebSocketServer()) webSocketServer.start();
    }



    public static webSocketServer getServer() {return webSocketServer;}

    public static StrengthConfig getStrengthConfig() {return StrengthConfig;}

    public static ModConfig getModConfig(){return modConfig;}


    //屏幕强度显示
    private void onHudRender(DrawContext drawContext, RenderTickCounter tickDelta) {
        MinecraftClient client = MinecraftClient.getInstance();

        if (client.player != null && client.world != null && (modConfig.getRenderingPositionX() < client.getWindow().getScaledWidth() || modConfig.getRenderingPositionY() < client.getWindow().getScaledHeight())) {
            // 假设强度数值是一个整数
//            int strengthValue = getStrengthValue(client.player);

            // 计算图标和文本的位置
            int x = modConfig.getRenderingPositionX();
            int y = modConfig.getRenderingPositionY();

            // 渲染图标
//            client.getTextureManager().bindTexture(STRENGTH_ICON);
//            drawTexture(drawContext, x, y, 0, 0, 16, 16);

            // 创建并渲染 OrderedText

            if(webSocketServer.getConnected()) {
                Text strengthText = Text.literal("A:" + webSocketServer.getStrength().getAStrength() + ",Max:" + webSocketServer.getStrength().getAMaxStrength());
                OrderedText orderedText = strengthText.asOrderedText();
                Text strengthText1 = Text.literal("B:" + webSocketServer.getStrength().getAStrength() + ",Max:" + webSocketServer.getStrength().getBMaxStrength());
                OrderedText orderedText1 = strengthText1.asOrderedText();
                drawContext.drawTextWithShadow(client.textRenderer, orderedText, x, y, 0xFFFFFF);
                drawContext.drawTextWithShadow(client.textRenderer, orderedText1, x, y + 9, 0xFFFFFF);
            }
            else {
                Text strengthText = Text.literal("未连接");
                OrderedText orderedText = strengthText.asOrderedText();
                drawContext.drawTextWithShadow(client.textRenderer, orderedText, x, y, 0xFF0000);
            }
        }
    }


}
