package a7.hyperion.mixin;

import a7.hyperion.AllItems;
import a7.hyperion.mixin.accessor.LocalPlayerAccessor;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LocalPlayer.class)
public class LocalPlayerMixin {
    @Redirect(method = "aiStep", at = @At(target = "Lnet/minecraft/client/player/LocalPlayer;isUsingItem()Z", value = "INVOKE"))
    public boolean hyperion$removeItemUsingSlowDown(LocalPlayer $this) {
        ItemStack stack = $this.getItemInHand($this.getUsedItemHand());
        if (stack.is(AllItems.TERMINATOR))
            return false;
        return ((LocalPlayerAccessor)$this).hyperion$getStartedUsingItem();
    }
}
