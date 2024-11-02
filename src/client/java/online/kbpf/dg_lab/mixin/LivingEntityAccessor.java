package online.kbpf.dg_lab.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import net.minecraft.entity.LivingEntity;

@Mixin(LivingEntity.class)
public interface LivingEntityAccessor {
    @Accessor("lastDamageTaken")
    float getLastDamageTaken();

    @Accessor("lastDamageTaken")
    void setLastDamageTaken(float value);


}
