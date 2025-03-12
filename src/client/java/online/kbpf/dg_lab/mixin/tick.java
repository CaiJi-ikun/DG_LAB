package online.kbpf.dg_lab.mixin;

import online.kbpf.dg_lab.client.entity.DGStrength;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static online.kbpf.dg_lab.client.Dg_labClient.*;


@Mixin(MinecraftClient.class)
public abstract class tick {

    @Shadow
    public abstract void tick();

    @Unique
    private int tickCounter = 0; // 计数器，用于跟踪游戏刻
    @Unique
    private int lastRunTickA = 0; // 上次为A执行的tick计数器值
    @Unique
    private int lastRunTickB = 0; // 上次为B执行的tick计数器值

    @Unique
    private boolean hasDetectedADelay = false; // 标志，表示是否检测到A的延迟第一次不为0
    @Unique
    private boolean hasDetectedBDelay = false; // 标志，表示是否检测到B的延迟第一次不为0

    @Unique
    private boolean ClearA = false; // 标志，表示是否检测到A的延迟第一次不为0
    @Unique
    private boolean ClearB = false; // 标志，表示是否检测到B的延迟第一次不为0

    @Unique
    private boolean hasDetectedADelayZeroAndStrength = false; // 标志，表示是否检测到A的延迟为0且强度大于0
    @Unique
    private boolean hasDetectedBDelayZeroAndStrength = false; // 标志，表示是否检测到B的延迟为0且强度大于0



    @Inject(method = "tick", at = @At("HEAD"))
    public void onTick(CallbackInfo info) {




        DGStrength dgStrength = webSocketServer.getStrength(); // 获取DGStrength对象
        int ADelayTime = dgStrength.getADelayTime(), BDelayTime = dgStrength.getBDelayTime(); // 获取A和B的等待时间


        // 更新等待时间
        ADelayTime = (ADelayTime > 0) ? ADelayTime - 1 : 0; // 如果ADelayTime大于0，减少1；否则设置为0
        BDelayTime = (BDelayTime > 0) ? BDelayTime - 1 : 0; // 如果BDelayTime大于0，减少1；否则设置为0
        webSocketServer.setDelayTime(ADelayTime, BDelayTime); // 设置更新后的等待时间

        int AStrength = dgStrength.getAStrength(), BStrength = dgStrength.getBStrength(); // 获取A和B的强度
        if (tickCounter % strengthConfig.getADownTime() == 0 && ADelayTime <= 0 && AStrength > 0) {
            // 如果计数器是ADownTime的倍数，且ADelayTime小于等于0且AStrength大于0，则发送A的强度值
            webSocketServer.sendStrengthToClient(strengthConfig.getADownValue(), 0, 1);
        }
        if (tickCounter % strengthConfig.getBDownTime() == 0 && BDelayTime <= 0 && BStrength > 0) {
            // 如果计数器是BDownTime的倍数，且BDelayTime小于等于0且BStrength大于0，则发送B的强度值
            webSocketServer.sendStrengthToClient(strengthConfig.getBDownValue(), 0, 2);
        }

        // 检查A的延迟时间和强度
        if (ADelayTime > 0) {
            hasDetectedADelayZeroAndStrength = false;
            ClearA = false;
            if (!hasDetectedADelay) {
                webSocketServer.sendDgWaveform(2, true, 1);
                hasDetectedADelay = true;
            } else if (tickCounter - lastRunTickA >= waveformMap.get("ADamage").getDuration() * 2) {
                webSocketServer.sendDgWaveform(2, false, 1);
                lastRunTickA = tickCounter;
            }
        } else {
            hasDetectedADelay = false;
            if (AStrength > 0) {
                if (!hasDetectedADelayZeroAndStrength) {
                    webSocketServer.sendDgWaveform(3, true, 1);
                    hasDetectedADelayZeroAndStrength = true;
                } else if (tickCounter - lastRunTickA >= waveformMap.get("AHealing").getDuration() * 2) {
                    webSocketServer.sendDgWaveform(3, false, 1);
                    lastRunTickA = tickCounter;
                }

            }
            else if(!ClearA){
                webSocketServer.CleanFrequency(1);
                ClearA = true;
            }
        }

        if (BDelayTime > 0) {
            ClearB = false;
            hasDetectedBDelayZeroAndStrength = false;
            if (!hasDetectedBDelay) {
                webSocketServer.sendDgWaveform(2, true, 2);
                hasDetectedBDelay = true;
            } else if (tickCounter - lastRunTickB >= waveformMap.get("BDamage").getDuration() * 2) {
                webSocketServer.sendDgWaveform(2, false, 2);
                lastRunTickB = tickCounter;
            }
        } else {
            hasDetectedBDelay = false;
            if (BStrength > 0) {
                if (!hasDetectedBDelayZeroAndStrength) {
                    webSocketServer.sendDgWaveform(3, true, 2);
                    hasDetectedBDelayZeroAndStrength = true;
                } else if (tickCounter - lastRunTickB >= waveformMap.get("BHealing").getDuration() * 2) {
                    webSocketServer.sendDgWaveform(3, false, 2);
                    lastRunTickB = tickCounter;
                }

            }
            else if(!ClearB){
                webSocketServer.CleanFrequency(2);
                ClearB = true;
            }
        }

        tickCounter++; // 增加计数器



        if (tickCounter == 2147483625) tickCounter = 0; // 如果计数器达到2147483625，则重置为0
    }


}
