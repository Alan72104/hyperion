package a7.hyperion.block;

import a7.hyperion.AllBlockEntityTypes;
import a7.hyperion.data.EssenceType;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.PlayerHeadItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.PlayerHeadBlock;
import net.minecraft.world.level.block.PlayerWallHeadBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class EssenceBlock extends PlayerHeadBlock {
    public final EssenceType type;

    public EssenceBlock(EssenceType type, Properties properties) {
        super(properties);
        this.type = type;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (level instanceof ServerLevel serverLevel) {
            player.sendSystemMessage(Component.literal("You found a %s Essence!".formatted(type.getDisplayName())));
            var drops = getDrops(state, serverLevel, pos, level.getBlockEntity(pos));
            drops.removeIf(player::addItem);
            drops.forEach(stack -> popResource(level, pos, stack));
            level.removeBlock(pos, false);
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return AllBlockEntityTypes.SKULL.create(pos, state);
    }

    public static class EssenceWallBlock extends PlayerWallHeadBlock {
        public final EssenceType type;

        public EssenceWallBlock(EssenceType type, Properties p) {
            super(p);
            this.type = type;
        }

        @Override
        public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
            return AllBlockEntityTypes.SKULL.create(pos, state);
        }
    }

    public static class EssenceItem extends PlayerHeadItem {
        public EssenceItem(EssenceBlock block, EssenceWallBlock wallBlock, Properties properties) {
            super(block, wallBlock, properties);
        }
    }

    public static class MySkullBlockEntity extends SkullBlockEntity {
        public MySkullBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
            super(pos, blockState);
//            ((BlockEntityAccessor) this).hyperion$setType(type);
        }

        @Override
        public BlockEntityType<?> getType() {
            return AllBlockEntityTypes.SKULL.get();
        }
    }
}
