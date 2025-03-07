package a7.hyperion;

import a7.hyperion.item.AspectOfTheVoidItem;
import a7.hyperion.item.DungeonMapItem;
import a7.hyperion.item.HyperionItem;
import a7.hyperion.item.TerminatorItem;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateItemModelProvider;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.nullness.NonNullConsumer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tiers;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;

import static a7.hyperion.Hyperion.REGISTRATE;

public class AllItems {
    public static final ItemEntry<HyperionItem> HYPERION = REGISTRATE.item("hyperion",
                    p -> new HyperionItem(Tiers.IRON, p))
            .model(AllItems::makeHandheldItemModel)
            .register();
    public static final ItemEntry<AspectOfTheVoidItem> ASPECT_OF_THE_VOID = REGISTRATE.item("aspect_of_the_void",
                    p -> new AspectOfTheVoidItem(Tiers.DIAMOND, p))
            .model(AllItems::makeHandheldItemModel)
            .register();
    public static final ItemEntry<TerminatorItem> TERMINATOR = REGISTRATE.item("terminator", TerminatorItem::new)
            .properties(p -> p.durability(1000))
            .tab(AllTabs.HYPERION.getKey(), TerminatorItem::addToTab)
            .onRegisterAfter(Registries.ITEM, addTooltip("Hello"))
            .model(AllItems::makeHandheldItemModel)
            .register();
    public static final ItemEntry<DungeonMapItem> DUNGEON_MAP = REGISTRATE.item("dungeon_map", DungeonMapItem::new)
            .model((ctx, output) -> output.generated(ctx::get, mc("item/filled_map")))
            .register();
    public static final ItemEntry<Item> ENCHANTED_ENDER_PEARL = REGISTRATE.item("enchanted_ender_pearl", Item::new)
            .properties(p -> p.component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true))
            .model((ctx, output) -> output.generated(ctx::get, mc("item/ender_pearl")))
            .register();
    public static final ItemEntry<Item> ABSOLUTE_ENDER_PEARL = REGISTRATE.item("absolute_ender_pearl", Item::new)
            .register();
    public static final ItemEntry<Item> TESSELLATED_ENDER_PEARL = REGISTRATE.item("tessellated_ender_pearl", Item::new)
            .register();
    public static final ItemEntry<Item> ENCHANTED_LAPIS_LAZULI = REGISTRATE.item("enchanted_lapis_lazuli", Item::new)
            .properties(p -> p.component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true))
            .model((ctx, output) -> output.generated(ctx::get, mc("item/lapis_lazuli")))
            .register();
    public static final ItemEntry<Item> JUDGEMENT_CORE = REGISTRATE.item("judgement_core", Item::new).register();
    public static final ItemEntry<Item> TREASURE_TALISMAN = REGISTRATE.item("treasure_talisman", Item::new).register();
    public static final ItemEntry<Item> TREASURE_RING = REGISTRATE.item("treasure_ring", Item::new).register();
    public static final ItemEntry<Item> TREASURE_ARTIFACT = REGISTRATE.item("treasure_artifact", Item::new).register();

    public static void register() {
    }

    private static ResourceLocation mc(String loc) {
        return ResourceLocation.withDefaultNamespace(loc);
    }

    private static NonNullConsumer<Item> addTooltip(String... tooltip) {
        return item -> {
            if (FMLEnvironment.dist == Dist.CLIENT) {
                HyperionClient.TOOLTIPS.put(item, tooltip);
            }
        };
    }

    private static <T extends Item> void makeHandheldItemModel(DataGenContext<Item, T> ctx, RegistrateItemModelProvider output) {
        output.withExistingParent(ctx.getName(), mc("item/handheld"))
                .texture("layer0", output.itemTexture(ctx::get));
    }
}
