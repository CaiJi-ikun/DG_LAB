package buzz.kbpf.dg_lab.mixin;


import buzz.kbpf.dg_lab.client.Dg_labClient;
import buzz.kbpf.dg_lab.client.entity.DGStrength;
import buzz.kbpf.dg_lab.client.entity.StrengthConfig;
import buzz.kbpf.dg_lab.client.webSocketServer.webSocketServer;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;



@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin extends AbstractClientPlayerEntity {

    @Unique
    float Dg_labHealth = 0.0f;

    public ClientPlayerEntityMixin(ClientWorld world, GameProfile profile) {
        super(world, profile);
    }

    @Inject(method = "updateHealth", at = @At("TAIL"))
    private void afterSetHealth(float health, CallbackInfo ci) {
        LivingEntityAccessor accessor = (LivingEntityAccessor) this;
        ClientPlayerEntityAccessor accessor1 = (ClientPlayerEntityAccessor) this;
        webSocketServer server = Dg_labClient.getServer();
        StrengthConfig StrengthConfig = Dg_labClient.getStrengthConfig();
        if (server != null && server.getConnected()) {
            float damage = Dg_labHealth - health;


            if (damage > 0.0F) {
                server.setDelayTime(StrengthConfig.getADelayTime(), StrengthConfig.getBDelayTime());
                if(StrengthConfig.getADamageStrength() > 0) server.sendStrengthToClient(Math.max(1, ((int) (damage * StrengthConfig.getADamageStrength()))), 1, 1);
                if(StrengthConfig.getBDamageStrength() > 0) server.sendStrengthToClient(Math.max(1, ((int) (damage * StrengthConfig.getBDamageStrength()))), 1, 2);
            }
            if (health <= 0) {
                server.setDelayTime(StrengthConfig.getADeathDelay(), StrengthConfig.getBDeathDelay());
                DGStrength dgStrength = server.getStrength();
                server.sendStrengthToClient((Math.min(dgStrength.getAStrength() + StrengthConfig.getADeathStrength(), dgStrength.getAMaxStrength())), 2, 1);
                server.sendStrengthToClient((Math.min(dgStrength.getBStrength() + StrengthConfig.getBDeathStrength(), dgStrength.getBMaxStrength())), 2, 2);
            }

            Dg_labHealth = health;
        }

    }

}

