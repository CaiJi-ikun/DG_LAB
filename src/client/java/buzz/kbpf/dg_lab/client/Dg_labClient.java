package buzz.kbpf.dg_lab.client;

import buzz.kbpf.dg_lab.client.entity.DGStrength;
import buzz.kbpf.dg_lab.client.entity.ModConfig;
import buzz.kbpf.dg_lab.client.entity.StrengthConfig;
import buzz.kbpf.dg_lab.client.webSocketServer.webSocketServer;
import buzz.kbpf.dg_lab.client.createQR.ToolQR;
import com.google.gson.Gson;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.text.Text;


import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;


import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.*;

public class Dg_labClient implements ClientModInitializer {

    private static final webSocketServer webSocketServer = new webSocketServer(new InetSocketAddress(9999));
    private static StrengthConfig StrengthConfig = new StrengthConfig();
    private static ModConfig modConfig = new ModConfig();
//    private static KeyBinding keyBinding;

    @Override
    public void onInitializeClient() {

        StrengthConfig = buzz.kbpf.dg_lab.client.entity.StrengthConfig.loadJson();
        modConfig = ModConfig.loadJson();

//        keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
//                "key.examplemod.spook", // The translation key of the keybinding's name
//                InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
//                GLFW.GLFW_KEY_R, // The keycode of the key
//                "category.examplemod.test" // The translation key of the keybinding's category.
//        ));

//        ClientTickEvents.END_CLIENT_TICK.register(client -> {
//            while (keyBinding.wasPressed()) {
//                client.player.sendMessage(Text.literal("Key 1 was pressed!"), false);
//            }
//        });

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> dispatcher.register(literal("dglab")
                .executes(context -> {
                            context.getSource().sendFeedback(Text.literal("此mod还在测试版"));
                            context.getSource().sendFeedback(Text.literal("需使用请用/dglab createQR创建二维码"));
                            context.getSource().sendFeedback(Text.literal("config配置强度设置"));
                            context.getSource().sendFeedback(Text.literal("WebSocketServer配置连接服务器设置"));
                            context.getSource().sendFeedback(Text.literal("设置自动保存"));
                            return 1;
                        }
                )
                .then(literal("createQR")
                        .executes(context -> {
                            ToolQR.CreateQR(context);
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
        ));

        if(modConfig.getAutoStartWebSocketServer()) webSocketServer.start();
    }



    public static webSocketServer getServer() {
        return webSocketServer;
    }

    public static StrengthConfig getConfig() {
        return StrengthConfig;
    }

    public static void setConfig(StrengthConfig StrengthConfig) {
        Dg_labClient.StrengthConfig = StrengthConfig;
    }
}
