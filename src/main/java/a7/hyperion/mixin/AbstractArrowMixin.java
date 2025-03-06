package a7.hyperion.mixin;

import a7.hyperion.entity.TerminatorArrow;
import net.minecraft.world.entity.projectile.AbstractArrow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AbstractArrow.class)
public abstract class AbstractArrowMixin {
    @Redirect(method = "tick", at = @At(target = "Lnet/minecraft/world/entity/projectile/AbstractArrow;isCritArrow()Z", value = "INVOKE"))
    public boolean hyperion$disableCritParticle(AbstractArrow $this) {
        if ($this instanceof TerminatorArrow)
            return false;
        return $this.isCritArrow();
    }
}
