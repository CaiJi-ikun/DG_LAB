package online.kbpf.dg_lab.mixin;

import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.util.math.BlockPos;
import online.kbpf.dg_lab.client.Config.StrengthConfig;
import online.kbpf.dg_lab.client.Dg_labClient;
import online.kbpf.dg_lab.client.webSocketServer.webSocketServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// +++ (由此開始) +++
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
// +++ (到此結束) +++

@Mixin(ClientPlayerInteractionManager.class)
public class ClientPlayerInteractionManagerMixin {

    @Inject(method = "breakBlock", at = @At("HEAD"))
    private void onBreakBlock(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        // 從 Dg_labClient 獲取 server 和 config 的實例
        webSocketServer server = Dg_labClient.getServer();
        StrengthConfig strengthConfig = Dg_labClient.getStrengthConfig();

        // 檢查 server 是否存在且已連接
        if (server != null && server.getConnected()) {
            
            // 獲取破壞方塊的強度和延遲（這些是我們即將在 StrengthConfig 中新增的）
            float aStrength = strengthConfig.getABreakBlockStrength();
            float bStrength = strengthConfig.getBBreakBlockStrength();
            int aDelay = strengthConfig.getABreakBlockDelay();
            int bDelay = strengthConfig.getBBreakBlockDelay();

            // 設置延遲，這將觸發 tick mixin 中的波形播放
            server.setDelayTime(aDelay, bDelay);

            // 發送增加強度的訊號，邏輯同 ClientPlayerEntityMixin
            // mode = 1 代表增加強度
            if (aStrength > 0) {
                server.sendStrengthToClient(Math.max(1, (int) aStrength), 1, 1);
            }
            if (bStrength > 0) {
                server.sendStrengthToClient(Math.max(1, (int) bStrength), 1, 2);
            }
        }
    }

    // +++ 攻擊生物反饋 (由此開始) +++
    @Inject(method = "attackEntity", at = @At("HEAD"))
    private void onAttackEntity(PlayerEntity player, Entity target, CallbackInfo ci) {
        // 從 Dg_labClient 獲取 server 和 config 的實例
        webSocketServer server = Dg_labClient.getServer();
        StrengthConfig strengthConfig = Dg_labClient.getStrengthConfig();

        // 檢查 server 是否存在且已連接
        if (server != null && server.getConnected()) {
            
            // 獲取攻擊生物的強度和延遲
            float aStrength = strengthConfig.getAAttackEntityStrength();
            float bStrength = strengthConfig.getBAttackEntityStrength();
            int aDelay = strengthConfig.getAAttackEntityDelay();
            int bDelay = strengthConfig.getBAttackEntityDelay();

            // 設置延遲
            server.setDelayTime(aDelay, bDelay);

            // 發送增加強度的訊號
            if (aStrength > 0) {
                server.sendStrengthToClient(Math.max(1, (int) aStrength), 1, 1);
            }
            if (bStrength > 0) {
                server.sendStrengthToClient(Math.max(1, (int) bStrength), 1, 2);
            }
        }
    }
    // +++ (到此結束) +++
}
