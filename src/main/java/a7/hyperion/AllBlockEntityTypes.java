package a7.hyperion;

import a7.hyperion.block.EssenceBlock;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
import net.minecraft.world.level.block.entity.BlockEntity;

import static a7.hyperion.Hyperion.REGISTRATE;

public class AllBlockEntityTypes {
    @SuppressWarnings("unchecked")
    public static final BlockEntityEntry<BlockEntity> SKULL = REGISTRATE.blockEntity(
                    "skull",
                    (type, pos, state) -> new EssenceBlock.MySkullBlockEntity(type, pos, state))
            .validBlocks(AllBlocks.WITHER_ESSENCE_BLOCK::get,
                    AllBlocks.WITHER_ESSENCE_WALL_BLOCK::get)
            .renderer(() -> ctx -> (BlockEntityRenderer<BlockEntity>) (Object) new SkullBlockRenderer(ctx))
            .register();

    public static void register() {
    }
}
