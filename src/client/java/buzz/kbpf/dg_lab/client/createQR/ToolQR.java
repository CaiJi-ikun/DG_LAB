package buzz.kbpf.dg_lab.client.createQR;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import net.minecraft.text.Text;

import javax.imageio.ImageIO;


public class ToolQR {
    private ToolQR() {
    }

    public static void CreateQR(com.mojang.brigadier.context.CommandContext<net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource> context) {
        try {
            InetAddress localhost = InetAddress.getLocalHost();
            String ipAddress = localhost.getHostAddress();
            String url = "https://www.dungeon-lab.com/app-download.php#DGLAB-SOCKET#ws://" + ipAddress + ":9999/1234-123456789-12345-12345-01";
            String filePath = "QR.png";
            try {
                Map<EncodeHintType, Object> hints = new HashMap<>();
                hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
                hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

                BitMatrix bitMatrix = new MultiFormatWriter().encode(url, BarcodeFormat.QR_CODE, 300, 300, hints);

                BufferedImage image = new BufferedImage(300, 300, BufferedImage.TYPE_INT_RGB);
                for (int x = 0; x < 300; x++) {
                    for (int y = 0; y < 300; y++) {
                        image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
                    }
                }

                File qrCodeFile = new File(filePath);
                ImageIO.write(image, "png", qrCodeFile);
                context.getSource().sendFeedback(Text.literal("二维码已保存到：" + qrCodeFile.getAbsolutePath()));

                File imageFile = new File("QR.png");

                // 检查 Desktop 类是否支持打开文件
                if (Desktop.isDesktopSupported()) {
                    Desktop desktop = Desktop.getDesktop();
                    try {
                        // 使用系统默认应用程序打开图片
                        desktop.open(imageFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("Desktop class is not supported on this platform.");
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (UnknownHostException e) {
            context.getSource().sendError(Text.literal("获取本地ip错误"));
        }


    }
}
