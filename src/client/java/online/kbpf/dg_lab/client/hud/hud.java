package online.kbpf.dg_lab.client.hud;

import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import static online.kbpf.dg_lab.client.Dg_labClient.modConfig;
import static online.kbpf.dg_lab.client.Dg_labClient.webSocketServer;

public class hud implements HudElement {



    //屏幕强度显示
    @Override
    public void render(DrawContext drawContext, RenderTickCounter tickDelta) {

        MinecraftClient client = MinecraftClient.getInstance();
        // 在onHudRender方法开头添加测试渲染
//        drawContext.drawTextWithShadow(
//                client.textRenderer,
//                Text.literal("测试文本"),
//                10, 10,
//                0xFF00FF00 // 绿色
//        );
        if (client.player != null && client.world != null && (modConfig.getRenderingPositionX() < client.getWindow().getScaledWidth() || modConfig.getRenderingPositionY() < client.getWindow().getScaledHeight())) {

            // 假设强度数值是一个整数
            //            int strengthValue = getStrengthValue(client.player);

            // 计算图标和文本的位置
            int x = modConfig.getRenderingPositionX();
            int y = modConfig.getRenderingPositionY();


            // 创建并渲染 OrderedText

            if(webSocketServer.getConnected()) {
                Text strengthText;
                Text strengthText1;
                if(modConfig.isRenderingMax()) {
                    strengthText = Text.literal("A:" + webSocketServer.getStrength().getAStrength() + ",Max:" + webSocketServer.getStrength().getAMaxStrength());

                    strengthText1 = Text.literal("B:" + webSocketServer.getStrength().getBStrength() + ",Max:" + webSocketServer.getStrength().getBMaxStrength());

                }
                else {
                    strengthText = Text.literal("A:" + webSocketServer.getStrength().getAStrength());

                    strengthText1 = Text.literal("B:" + webSocketServer.getStrength().getBStrength());
                }
                drawContext.drawTextWithShadow(client.textRenderer, strengthText, x, y, 0xFFFFFFFF);
                drawContext.drawTextWithShadow(client.textRenderer, strengthText1, x, y + 9, 0xFFFFFFFF);
            }
            else {
                Text strengthText = Text.literal("未连接");
                drawContext.drawTextWithShadow(client.textRenderer, strengthText, x, y, 0xFFFF0000);
            }
        }
    }
}
