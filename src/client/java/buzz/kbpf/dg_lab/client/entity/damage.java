package buzz.kbpf.dg_lab.client.entity;

import net.minecraft.entity.damage.DamageSource;

public class damage {
    private float value;
    private DamageSource damageSource;
    public damage() {
    }

    public damage(int value, DamageSource damageSource) {
        this.value = value;
        this.damageSource = damageSource;
    }

    public float getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public DamageSource getDamageSource() {
        return damageSource;
    }

    public void setDamageSource(DamageSource damageSource) {
        this.damageSource = damageSource;
    }
}
