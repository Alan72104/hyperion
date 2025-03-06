package a7.hyperion;

import a7.hyperion.block.BouncyLavaBlock;
import com.tterrag.registrate.util.entry.FluidEntry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;

import static a7.hyperion.Hyperion.REGISTRATE;

public class AllFluids {
    public static FluidEntry<BaseFlowingFluid.Flowing> BOUNCY_LAVA = REGISTRATE
            .fluid("bouncy_lava", mc("block/lava_still"), mc("block/lava_flow"))
            .fluidProperties(p -> p
                    .tickRate(30)
                    .explosionResistance(100)
                    .levelDecreasePerBlock(2)
                    .slopeFindDistance(2))
            .source(BaseFlowingFluid.Source::new)

            .block(BouncyLavaBlock::new)
            .properties(p -> BlockBehaviour.Properties.ofFullCopy(Blocks.LAVA))
            .build()

            .bucket()
            .model((ctx, output) -> output.generated(ctx::get, mc("item/lava_bucket")))
            .properties(p -> p.craftRemainder(Items.BUCKET).stacksTo(1))
            .build()

            .tag(FluidTags.LAVA)
            .register();

    public static void register() {
    }

    private static ResourceLocation mc(String loc) {
        return ResourceLocation.withDefaultNamespace(loc);
    }

}
