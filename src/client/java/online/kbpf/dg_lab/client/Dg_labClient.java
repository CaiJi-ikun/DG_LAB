package online.kbpf.dg_lab.client;

import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.util.Identifier;
import online.kbpf.dg_lab.client.Tool.DGWaveformTool;
import online.kbpf.dg_lab.client.command.Default;
import online.kbpf.dg_lab.client.Config.ModConfig;
import online.kbpf.dg_lab.client.Config.StrengthConfig;
import online.kbpf.dg_lab.client.Config.WaveformConfig;
import online.kbpf.dg_lab.client.entity.Waveform.Waveform;
import online.kbpf.dg_lab.client.hud.hud;
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
import java.util.Map;



public class Dg_labClient implements ClientModInitializer {

    public static webSocketServer webSocketServer = null;
    public static StrengthConfig strengthConfig = new StrengthConfig();
    public static final ModConfig modConfig = ModConfig.loadJson();
    public static Map<String, Waveform> waveformMap = WaveformConfig.LoadWaveform();

    private static KeyBinding keyBinding;
    private final Screen configScreen = new ConfigScreen();

    private static final Identifier HUD_ID = Identifier.of("dglab", "hud");


    @Override
    public void onInitializeClient() {



        //注册连接的服务器
        webSocketServer = new webSocketServer(new InetSocketAddress(modConfig.getServerPort()));

        strengthConfig = online.kbpf.dg_lab.client.Config.StrengthConfig.loadJson();

        DGWaveformTool.updateDuration();

        hud tntHud = new hud();
        HudElementRegistry.addLast(HUD_ID, tntHud);

        keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "打开配置界面",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_O,
                "DG_LAB"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (keyBinding.wasPressed()) {
                client.setScreen(configScreen);
            }
        });
        //指令定义
        Default.register(modConfig, strengthConfig, webSocketServer);

        if(modConfig.getAutoStartWebSocketServer()) webSocketServer.start();
    }



    public static webSocketServer getServer() {return webSocketServer;}

    public static StrengthConfig getStrengthConfig() {return strengthConfig;}

    public static ModConfig getModConfig(){return modConfig;}





}
