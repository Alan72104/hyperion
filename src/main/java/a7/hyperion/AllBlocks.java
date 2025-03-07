package a7.hyperion;

import a7.hyperion.block.EssenceBlock;
import a7.hyperion.data.EssenceType;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyComponentsFunction;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;

import static a7.hyperion.Hyperion.REGISTRATE;

public class AllBlocks {
    public static final BlockEntry<Block> EXAMPLE_BLOCK = REGISTRATE.block("example_block", Block::new)
            .properties(p -> p.mapColor(MapColor.STONE))
            .simpleItem()
            .register();
    public static final BlockEntry<Block> ENCHANTED_LAPIS_BLOCK = REGISTRATE.block("enchanted_lapis_block", Block::new)
            .initialProperties(() -> Blocks.LAPIS_BLOCK)
            .properties(p -> p.mapColor(MapColor.LAPIS).lightLevel(state -> 15))
            .blockstate((ctx, output) -> output.simpleBlock(ctx.get(), output.cubeAll(Blocks.LAPIS_BLOCK)))
            .item()
            .properties(p -> p.component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true))
            .build()
            .register();
    public static final BlockEntry<Block> SPAWN_ANCHOR = REGISTRATE.block("spawn_anchor", properties -> new Block(properties))
            .properties(p -> p
                    .noCollission()
                    .lightLevel(state -> 8)
                    .strength(-1, 3600000))
            .blockstate((ctx, output) -> output
                    .simpleBlock(ctx.get(), output.models()
                            .cubeAll(ctx.getName(), output.blockTexture(ctx.get()))
                            .renderType(RenderType.translucent().name)))
            .simpleItem()
            .register();
    public static final BlockEntry<EssenceBlock.EssenceWallBlock> WITHER_ESSENCE_WALL_BLOCK = REGISTRATE.block("wither_essence_wall_block",
                    p -> new EssenceBlock.EssenceWallBlock(EssenceType.WITHER, p))
            .initialProperties(() -> Blocks.PLAYER_WALL_HEAD)
            .lang(block -> "block.hyperion.wither_essence_wall_block", "Wither Essence")
            .loot((lt, block) -> lt.add(block, makeSkullLootTable(block)))
            .blockstate(AllBlocks::makeSkullBlockstate)
            .register();
    public static final BlockEntry<EssenceBlock> WITHER_ESSENCE_BLOCK = REGISTRATE.block("wither_essence",
                    p -> new EssenceBlock(EssenceType.WITHER, p))
            .initialProperties(() -> Blocks.PLAYER_HEAD)
            .loot((lt, block) -> lt.add(block, makeSkullLootTable(block)))
            .blockstate(AllBlocks::makeSkullBlockstate)
            .item((block, p) -> makeEssenceItem(p))
            .properties(p -> p.rarity(Rarity.UNCOMMON).component(DataComponents.PROFILE, EssenceType.WITHER.getProfile()))
            .model((ctx, output) -> output.withExistingParent(ctx.getName(), "template_skull"))
            .build()
            .register();

    public static void register() {
    }

    private static ResourceLocation mc(String path) {
        return ResourceLocation.withDefaultNamespace(path);
    }

    private static EssenceBlock.EssenceItem makeEssenceItem(Item.Properties p) {
        return new EssenceBlock.EssenceItem(WITHER_ESSENCE_BLOCK.get(), WITHER_ESSENCE_WALL_BLOCK.get(), p);
    }

    private static LootTable.Builder makeSkullLootTable(Block block) {
        return LootTable.lootTable().withPool(LootPool.lootPool()
                .when(ExplosionCondition.survivesExplosion())
                .setRolls(ConstantValue.exactly(1.0F))
                .add(
                        LootItem.lootTableItem(block)
                                .apply(
                                        CopyComponentsFunction.copyComponents(CopyComponentsFunction.Source.BLOCK_ENTITY)
                                                .include(DataComponents.PROFILE)
                                                .include(DataComponents.NOTE_BLOCK_SOUND)
                                                .include(DataComponents.CUSTOM_NAME)
                                )
                )
        );
    }

    private static <T extends Block> void makeSkullBlockstate(DataGenContext<Block, T> ctx, RegistrateBlockstateProvider output) {
        output.getVariantBuilder(ctx.get())
                .partialState()
                .setModels(new ConfiguredModel(new ModelFile.ExistingModelFile(mc("block/skull"), output.models().existingFileHelper)));
    }
}
