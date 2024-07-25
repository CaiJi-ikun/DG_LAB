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
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;



@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin extends AbstractClientPlayerEntity {

    @Shadow public abstract void heal(float amount);

    public ClientPlayerEntityMixin(ClientWorld world, GameProfile profile) {
        super(world, profile);
    }

    @Inject(method = "updateHealth", at = @At("HEAD"))
    private void afterSetHealth(float health, CallbackInfo ci) {
        LivingEntityAccessor accessor = (LivingEntityAccessor) this;
        ClientPlayerEntityAccessor accessor1 = (ClientPlayerEntityAccessor) this;



        if (accessor1.getHealthInitialized()){


            webSocketServer server = Dg_labClient.getServer();
            StrengthConfig StrengthConfig = Dg_labClient.getconfig();
            if (server != null && server.getConnected() && accessor1.getHealthInitialized()) {
                float damage = this.getHealth() - health;
                System.out.println("hurt" + damage + " " + this.getHealth() + " " + health);


                if (damage > 0.0F) {
                    server.setDelayTime(StrengthConfig.getADelayTime(), StrengthConfig.getBDelayTime());
                    server.sendStrengthToClient((int) (damage * StrengthConfig.getADamageStrength()), 1, 1);
                    server.sendStrengthToClient((int) (damage * StrengthConfig.getBDamageStrength()), 1, 2);
                }
                if(this.getHealth() == 0){
                    DGStrength dgStrength = server.getStrength();
                    server.sendStrengthToClient((Math.min(dgStrength.getAStrength() + 50, dgStrength.getAMaxStrength())) , 2, 1);
                    server.sendStrengthToClient((Math.min(dgStrength.getBStrength() + 50, dgStrength.getBMaxStrength())) , 2, 2);
                }
            }
        }
    }
}

