package online.kbpf.dg_lab.client.createQR;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.text.TextColor;
import online.kbpf.dg_lab.client.Dg_labClient;
import online.kbpf.dg_lab.client.Config.ModConfig;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import javax.imageio.ImageIO;


public class ToolQR {
    private ToolQR() {
    }

    public static void CreateQR() {
        ModConfig modConfig = Dg_labClient.getModConfig();
        String ipAddress = modConfig.getAddress();
        if(ipAddress.equals("error")) {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.player != null) {
                client.player.sendMessage(Text.literal("没有指定的ip地址").styled(style -> style.withColor(TextColor.fromRgb(0xFF5555))), false);
            }
        }
        else {
            int port = modConfig.getPort();
            StringBuilder url = new StringBuilder("https://www.dungeon-lab.com/app-download.php#DGLAB-SOCKET#ws://").append(ipAddress).append(':').append(port).append("/1234-123456789-12345-12345-01");
            String filePath = "QR.png";
            try {
                Map<EncodeHintType, Object> hints = new HashMap<>();
                hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
                hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

                BitMatrix bitMatrix = new MultiFormatWriter().encode(url.toString(), BarcodeFormat.QR_CODE, 300, 300, hints);

                BufferedImage image = new BufferedImage(300, 300, BufferedImage.TYPE_INT_RGB);
                for (int x = 0; x < 300; x++) {
                    for (int y = 0; y < 300; y++) {
                        image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
                    }
                }

                File qrCodeFile = new File(filePath);
                ImageIO.write(image, "png", qrCodeFile);


                ProcessBuilder pb = new ProcessBuilder("cmd", "/c", "start", "", "\"" + qrCodeFile.getAbsolutePath() + "\"");
                pb.start();


            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }
}
