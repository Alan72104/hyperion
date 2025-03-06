package a7.hyperion.item;

import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.EntityBasedExplosionDamageCalculator;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class HyperionItem extends SwordItem implements ITeleportTool {
    public HyperionItem(Tier tier, Properties properties) {
        super(tier, properties);
    }

    @Override
    public float getTeleportDistance() {
        return 7;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (!level.isClientSide) {
            Vec3 pos = teleportForward(level, player);
            level.explode(player, null, new EntityBasedExplosionDamageCalculator(player) {
                @Override
                public boolean shouldDamageEntity(Explosion explosion, Entity entity) {
                    return !entity.is(player) && !(entity instanceof ItemEntity);
                }
            }, pos, 3, false, Level.ExplosionInteraction.NONE);
            player.awardStat(Stats.ITEM_USED.get(this));
        }
        return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
    }
}
