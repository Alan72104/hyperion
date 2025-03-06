package a7.hyperion.entity;

import a7.hyperion.AllEntityTypes;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public class TerminatorArrow extends Arrow {
    public TerminatorArrow(EntityType<TerminatorArrow> entityType, Level level) {
        super(entityType, level);
    }

    public TerminatorArrow(Level level, LivingEntity owner, ItemStack pickupItemStack, @Nullable ItemStack firedFromWeapon) {
        super(level, owner, pickupItemStack, firedFromWeapon);
    }

    @Override
    public EntityType<?> getType() {
        return AllEntityTypes.TERMINATOR_ARROW.get();
    }

    @Override
    public void tick() {
        super.tick();
        if (this.inGround)
            this.discard();
    }

    @SuppressWarnings("resource")
    @Override
    protected void makeParticle(int particleAmount) {
        if (!this.inGround) {
            this.level().addParticle(ParticleTypes.FALLING_LAVA, this.getX(), this.getY(), this.getZ(), 0, 0, 0);
            this.level().addParticle(DustParticleOptions.REDSTONE, this.getX(), this.getY(), this.getZ(), 0, 0, 0);
        }
    }
}
