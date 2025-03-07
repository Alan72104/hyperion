package a7.hyperion.datagen;

import a7.hyperion.AllBlocks;
import a7.hyperion.AllItems;
import a7.hyperion.Hyperion;
import a7.hyperion.item.TerminatorItem;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import java.util.concurrent.CompletableFuture;

public class MyRecipeProvider extends RecipeProvider {
    public MyRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider);
    }

    private static TagKey<Item> c(String path) {
        return ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", path));
    }

    private static ResourceLocation getItemKey(ItemLike item) {
        return BuiltInRegistries.ITEM.getKey(item.asItem());
    }

    private static MyShapedRecipeBuilder shaped(RecipeCategory category, ItemLike result) {
        return shaped(category, result, 1);
    }

    private static MyShapedRecipeBuilder shaped(RecipeCategory category, ItemLike result, int count) {
        return new MyShapedRecipeBuilder(category, result, count);
    }

    private static MyShapedRecipeBuilder shaped(RecipeCategory category, ItemStack result) {
        return new MyShapedRecipeBuilder(category, result);
    }

    private static void packUnpack3x3(RecipeCategory category, ItemLike result, TagKey<Item> from, ItemLike unpackTo, RecipeOutput output) {
        pack3x3(category, result, from, output);
        unpack3x3(category, unpackTo, result, output);
    }

    private static void packUnpack3x3(RecipeCategory category, ItemLike result, ItemLike from, RecipeOutput output) {
        pack3x3(category, result, from, output);
        unpack3x3(category, from, result, output);
    }

    private static void pack3x3(RecipeCategory category, ItemLike result, TagKey<Item> from, RecipeOutput output) {
        ShapelessRecipeBuilder.shapeless(category, result)
                .requires(Ingredient.of(from), 9)
                .unlockedBy("has_item", has(from))
                .save(output, Hyperion.loc(getItemKey(result).getPath() + "_from_" +
                        from.location().getPath().replace('/', '_')));
    }

    private static void pack3x3(RecipeCategory category, ItemLike result, ItemLike from, RecipeOutput output) {
        ShapelessRecipeBuilder.shapeless(category, result)
                .requires(from, 9)
                .unlockedBy("has_item", has(from))
                .save(output, Hyperion.loc(getItemKey(result).getPath() + "_from_" +
                        getItemKey(from).getPath().replace('/', '_')));
    }

    private static void unpack3x3(RecipeCategory category, ItemLike result, ItemLike from, RecipeOutput output) {
        ShapelessRecipeBuilder.shapeless(category, result, 9)
                .requires(from)
                .unlockedBy("has_item", has(from))
                .save(output, Hyperion.loc(getItemKey(result).getPath() + "_from_" +
                        getItemKey(from).getPath().replace('/', '_')));
    }

    @Override
    protected void buildRecipes(RecipeOutput output, HolderLookup.Provider registries) {
        shaped(RecipeCategory.COMBAT, AllItems.HYPERION)
                .pattern("X")
                .pattern("X")
                .pattern("#")
                .define('X', c("ingots/iron"))
                .define('#', c("rods/blaze"))
                .unlockedBy(c("ingots/iron"))
                .save(output);

        packUnpack3x3(RecipeCategory.MISC, AllItems.ENCHANTED_ENDER_PEARL, c("ender_pearls"), Items.ENDER_PEARL, output);
        packUnpack3x3(RecipeCategory.MISC, AllItems.ABSOLUTE_ENDER_PEARL, AllItems.ENCHANTED_ENDER_PEARL, output);
        shaped(RecipeCategory.MISC, AllItems.TESSELLATED_ENDER_PEARL)
                .pattern("#X#")
                .pattern("XXX")
                .pattern("#X#")
                .define('X', AllItems.ABSOLUTE_ENDER_PEARL)
                .define('#', AllBlocks.ENCHANTED_LAPIS_BLOCK)
                .unlockedBy(AllItems.ABSOLUTE_ENDER_PEARL)
                .save(output);

        packUnpack3x3(RecipeCategory.MISC, AllItems.ENCHANTED_LAPIS_LAZULI, c("gems/lapis"), Items.LAPIS_LAZULI, output);
        packUnpack3x3(RecipeCategory.MISC, AllBlocks.ENCHANTED_LAPIS_BLOCK, AllItems.ENCHANTED_LAPIS_LAZULI, output);

        shaped(RecipeCategory.MISC, AllItems.JUDGEMENT_CORE)
                .pattern(" X ")
                .pattern("XXX")
                .pattern(" X ")
                .define('X', AllItems.TESSELLATED_ENDER_PEARL)
                .unlockedBy(AllItems.TESSELLATED_ENDER_PEARL)
                .save(output);

        shaped(RecipeCategory.COMBAT, TerminatorItem.getDefaultEnchanted(registries, AllItems.TERMINATOR.get()))
                .pattern("EXS")
                .pattern("XCS")
                .pattern("EXS")
                .define('X', Items.STICK)
                .define('E', AllItems.TESSELLATED_ENDER_PEARL)
                .define('C', AllItems.JUDGEMENT_CORE)
                .define('S', Items.STRING)
                .unlockedBy(AllItems.JUDGEMENT_CORE)
                .save(output);

        shaped(RecipeCategory.MISC, AllItems.TREASURE_RING)
                .pattern("XXX")
                .pattern("X X")
                .pattern("XXX")
                .define('X', AllItems.TREASURE_TALISMAN)
                .unlockedBy(AllItems.TREASURE_TALISMAN)
                .save(output);
        pack3x3(RecipeCategory.MISC, AllItems.TREASURE_ARTIFACT, AllItems.TREASURE_RING, output);
    }

    private static class MyShapedRecipeBuilder extends ShapedRecipeBuilder {
        public MyShapedRecipeBuilder(RecipeCategory category, ItemLike result, int count) {
            super(category, result, count);
        }

        public MyShapedRecipeBuilder(RecipeCategory category, ItemStack result) {
            super(category, result);
        }

        public MyShapedRecipeBuilder unlockedBy(TagKey<Item> tag) {
            super.unlockedBy("has_item", has(tag));
            return this;
        }

        public MyShapedRecipeBuilder unlockedBy(ItemLike item) {
            super.unlockedBy("has_item", has(item));
            return this;
        }

        public MyShapedRecipeBuilder define(Character symbol, TagKey<Item> tag) {
            super.define(symbol, tag);
            return this;
        }

        public MyShapedRecipeBuilder define(Character symbol, ItemLike item) {
            super.define(symbol, item);
            return this;
        }

        public MyShapedRecipeBuilder define(Character symbol, Ingredient ingredient) {
            super.define(symbol, ingredient);
            return this;
        }

        public MyShapedRecipeBuilder pattern(String pattern) {
            super.pattern(pattern);
            return this;
        }
    }
}
