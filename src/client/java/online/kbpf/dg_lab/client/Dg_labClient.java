package online.kbpf.dg_lab.client;

import online.kbpf.dg_lab.client.Tool.FrequencyTool.FrequencyTool;
import online.kbpf.dg_lab.client.entity.DGStrength;
import online.kbpf.dg_lab.client.Tool.DGWaveformTool;
import online.kbpf.dg_lab.client.entity.ModConfig;
import online.kbpf.dg_lab.client.entity.StrengthConfig;
import online.kbpf.dg_lab.client.screen.ConfigScreen;
import online.kbpf.dg_lab.client.webSocketServer.webSocketServer;
import online.kbpf.dg_lab.client.createQR.ToolQR;
import com.google.gson.Gson;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
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

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;


import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.*;

public class Dg_labClient implements ClientModInitializer {

    private static online.kbpf.dg_lab.client.webSocketServer.webSocketServer webSocketServer = null;
    private static online.kbpf.dg_lab.client.entity.StrengthConfig StrengthConfig = new StrengthConfig();
    private static ModConfig modConfig = ModConfig.loadJson();
    private static KeyBinding keyBinding;
    private final Screen ConfigScreen = new ConfigScreen();

    @Override
    public void onInitializeClient() {

        webSocketServer = new webSocketServer(new InetSocketAddress(modConfig.getServerPort()));

        StrengthConfig = online.kbpf.dg_lab.client.entity.StrengthConfig.loadJson();
        HudRenderCallback.EVENT.register(this::onHudRender);

        keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "打开配置界面", // The translation key of the keybinding's name
                InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
                GLFW.GLFW_KEY_O, // The keycode of the key
                "DG_LAB" // The translation key of the keybinding's category.
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (keyBinding.wasPressed()) {

                client.setScreen(ConfigScreen);
            }
        });

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> dispatcher.register(literal("dglab")
                .executes(context -> {
                            context.getSource().sendFeedback(Text.literal("此mod还在测试版"));
                            context.getSource().sendFeedback(Text.literal("默认使用按键O打开配置页面"));
                            return 1;
                        }
                )
                .then(literal("createQR")
                        .executes(context -> {
                            ToolQR.CreateQR();
                            return 1;
                        }))
                .then(literal("Strength").then(literal("get")
                                .executes(context -> {
                                    context.getSource().sendFeedback(Text.literal(new Gson().toJson(webSocketServer.getStrength(), DGStrength.class)));
                                    return 1;
                                }))
                        .then(literal("set").then(argument("AStrength", IntegerArgumentType.integer()).then(argument("BStrength", IntegerArgumentType.integer()).executes(context -> {
                            DGStrength DGStrength = webSocketServer.getStrength();
                            DGStrength.setAStrength(IntegerArgumentType.getInteger(context, "AStrength"));
                            DGStrength.setBStrength(IntegerArgumentType.getInteger(context, "BStrength"));
                            webSocketServer.setStrength(DGStrength);
                            webSocketServer.sendStrength();
                            return 1;
                        })))))
                .then(literal("WebSocketServer")
                        .then(literal("start").executes(context -> {
                            try {
                                InetAddress localhost = InetAddress.getLocalHost();
                                String ipAddress = localhost.getHostAddress();
                                context.getSource().sendFeedback(Text.literal("本地ip:" + ipAddress));
                                context.getSource().sendFeedback(Text.literal("请确保连接的手机和此客户端在同一局域网下"));
                            } catch (UnknownHostException e) {
                                throw new RuntimeException(e);
                            }

                            webSocketServer.start();
                            return 1;
                        }))
                        .then(literal("AutoStart")
                                .executes(context -> {
                                    if (modConfig.getAutoStartWebSocketServer()) context.getSource().sendFeedback(Text.literal("自动启动已开启"));
                                    else context.getSource().sendFeedback(Text.literal("自动启动未开启"));
                                    return 1;
                                })
                                .then(argument("boolean", BoolArgumentType.bool())
                                        .executes(context -> {
                                            modConfig.setAutoStartWebSocketServer(BoolArgumentType.getBool(context, "boolean"));
                                            modConfig.savaFile();
                                            return 1;
                                        }))
                        )


                )
                .then(literal("config")
                        .executes(context -> {
                            context.getSource().sendFeedback(Text.literal("受伤时每半心增加的强度值A:" + StrengthConfig.getADamageStrength() + "B:" + StrengthConfig.getBDamageStrength()));
                            context.getSource().sendFeedback(Text.literal("没受伤多长时间开始下降强度A:" + StrengthConfig.getADelayTime() + "B:" + StrengthConfig.getBDelayTime()));
                            context.getSource().sendFeedback(Text.literal("下降强度时每次的间隔时间A:" + StrengthConfig.getADownTime() + "B:" + StrengthConfig.getBDownTime()));
                            context.getSource().sendFeedback(Text.literal("下降强度时每次的降低强度数值A:" + StrengthConfig.getADownValue() + "B:" + StrengthConfig.getBDownValue()));
                            context.getSource().sendFeedback(Text.literal("时间的单位为40ms,即1=40ms"));
                            return 1;
                        })
                        .then(literal("set")
                                .then(literal("DamageStrength")
                                        .then(argument("ADamageStrength", FloatArgumentType.floatArg()).then(argument("BDamageStrength", FloatArgumentType.floatArg())
                                                .executes(context -> {
                                                    StrengthConfig.setADamageStrength(FloatArgumentType.getFloat(context, "ADamageStrength"));
                                                    StrengthConfig.setBDamageStrength(FloatArgumentType.getFloat(context, "BDamageStrength"));
                                                    StrengthConfig.savaFile();
                                                    return 1;
                                                })
                                        ))
                                )
                                .then(literal("DelayTime")
                                        .then(argument("ADelayTime", IntegerArgumentType.integer()).then(argument("BDelayTime", IntegerArgumentType.integer())
                                                .executes(context -> {
                                                    StrengthConfig.setADelayTime(IntegerArgumentType.getInteger(context, "ADelayTime"));
                                                    StrengthConfig.setBDelayTime(IntegerArgumentType.getInteger(context, "BDelayTime"));
                                                    StrengthConfig.savaFile();
                                                    return 1;
                                                })
                                        ))
                                )
                                .then(literal("DownTime")
                                        .then(argument("ADownTime", IntegerArgumentType.integer()).then(argument("BDownTime", IntegerArgumentType.integer())
                                                .executes(context -> {
                                                    StrengthConfig.setADownTime(IntegerArgumentType.getInteger(context, "ADownTime"));
                                                    StrengthConfig.setBDownTime(IntegerArgumentType.getInteger(context, "BDownTime"));
                                                    StrengthConfig.savaFile();
                                                    return 1;
                                                })
                                        ))
                                )
                                .then(literal("DownValue")
                                        .then(argument("ADownValue", IntegerArgumentType.integer()).then(argument("BDownValue", IntegerArgumentType.integer())
                                                .executes(context -> {
                                                    StrengthConfig.setADownValue(IntegerArgumentType.getInteger(context, "ADownValue"));
                                                    StrengthConfig.setBDownValue(IntegerArgumentType.getInteger(context, "BDownValue"));
                                                    StrengthConfig.savaFile();
                                                    return 1;
                                                })
                                        ))
                                )
                        )
                )
                        .then(literal("test")
                                .then(argument(("text"), StringArgumentType.string()).executes(context -> {
                                    context.getSource().sendFeedback(Text.literal(FrequencyTool.toFrequency(StringArgumentType.getString(context, "test"))));
                                    return 1;
                                }))
                                .then(literal("send").then(argument(("text"), StringArgumentType.string()).executes(context -> {

                                    webSocketServer.sendDGWaveForm(DGWaveformTool.TextToWaveform(StringArgumentType.getString(context, "text")));
                                    return 1;
                                }))))

//                .then(literal("text").executes(context -> {
//                    context.getSource().sendFeedback(Text.literal(ConfigScreen));
//                    return 1;
//                }))
        ));

        if(modConfig.getAutoStartWebSocketServer()) webSocketServer.start();
    }



    public static webSocketServer getServer() {
        return webSocketServer;
    }

    public static StrengthConfig getStrengthConfig() {
        return StrengthConfig;
    }

    public static ModConfig getModConfig(){return modConfig;}



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
