package a7.hyperion;

import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;

import static a7.hyperion.Hyperion.REGISTRATE;

public class AllBlocks {
    public static final BlockEntry<Block> EXAMPLE_BLOCK = REGISTRATE.block("example_block", Block::new)
            .properties(p -> p.mapColor(MapColor.STONE))
            .simpleItem()
            .register();
    public static final BlockEntry<Block> ENCHANTED_LAPIS_BLOCK = REGISTRATE.block("enchanted_lapis_block", Block::new)
            .properties(p -> p.mapColor(MapColor.LAPIS).lightLevel(state -> 15))
            .blockstate((ctx, output) -> output.simpleBlock(ctx.getEntry(), output.cubeAll(Blocks.LAPIS_BLOCK)))
            .item()
            .properties(p -> p.component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true))
            .build()
            .register();
    public static final BlockEntry<Block> SPAWN_ANCHOR = REGISTRATE.block("spawn_anchor", Block::new)
            .properties(p -> p.noCollission())
            .simpleItem()
            .register();

    public static void register() {
    }
}
