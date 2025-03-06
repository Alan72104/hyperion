package a7.hyperion.item;

import a7.hyperion.entity.TerminatorArrow;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.util.CreativeModeTabModifier;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.function.Predicate;

public class TerminatorItem extends BowItem {
    private int shootDelay = 0;

    public TerminatorItem(Properties properties) {
        super(properties);
    }

    public static void addToTab(DataGenContext<Item, TerminatorItem> ctx, CreativeModeTabModifier output) {
        output.accept(getDefaultEnchanted(output.getParameters().holders(), ctx.get()));
    }

    public static ItemStack getDefaultEnchanted(HolderLookup.Provider registries, Item item) {
        HolderLookup.RegistryLookup<Enchantment> registry = registries.lookupOrThrow(Registries.ENCHANTMENT);
        ItemEnchantments.Mutable enchantments = new ItemEnchantments.Mutable(ItemEnchantments.EMPTY);
        enchantments.set(registry.getOrThrow(Enchantments.MULTISHOT), 1);
        enchantments.set(registry.getOrThrow(Enchantments.INFINITY), 1);
        enchantments.set(registry.getOrThrow(Enchantments.PIERCING), 4);
        var stack = new ItemStack(item);
        stack.set(DataComponents.ENCHANTMENTS, enchantments.toImmutable());
        return stack;
    }

    @Override
    public Predicate<ItemStack> getAllSupportedProjectiles(ItemStack stack) {
        return super.getAllSupportedProjectiles(stack).or(stack2 -> stack2.is(Items.ARROW));
    }

    @Override
    protected Projectile createProjectile(Level level, LivingEntity shooter, ItemStack weapon, ItemStack ammo, boolean isCrit) {
        Arrow arrow = new TerminatorArrow(level, shooter, ammo.copyWithCount(1), weapon);
        arrow.setCritArrow(true);
        return arrow;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        // Remove animation
        return UseAnim.NONE;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 72000;
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entityLiving, int timeLeft) {
        // No release
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        InteractionResultHolder<ItemStack> result = super.use(level, player, hand);
        if (result.getResult().consumesAction()) // Started using
            shootDelay = 0;
        return result;
    }

    @Override
    public void onUseTick(Level level, LivingEntity living, ItemStack stack, int remainingUseDuration) {
        super.onUseTick(level, living, stack, remainingUseDuration);
        shootDelay--;
        if (shootDelay <= 0) {
            shootDelay = 6;
            if (living instanceof Player player) {
                ItemStack itemstack = player.getProjectile(stack);
                if (!itemstack.isEmpty()) {
                    final float power = 1;
                    List<ItemStack> list = draw(stack, itemstack, player);
                    if (level instanceof ServerLevel serverlevel) {
                        if (!list.isEmpty())
                            this.shoot(serverlevel, player, player.getUsedItemHand(), stack, list, power * 3.0F, 1.0F, true, null);
                        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + power * 0.5F);
                        player.awardStat(Stats.ITEM_USED.get(this));
                    }
                }
            }
        }
    }
}
