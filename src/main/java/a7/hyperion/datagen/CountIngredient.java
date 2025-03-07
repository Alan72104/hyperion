package a7.hyperion.datagen;

import a7.hyperion.AllIngredientTypes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.crafting.ICustomIngredient;
import net.neoforged.neoforge.common.crafting.IngredientType;

import java.util.Arrays;
import java.util.stream.Stream;

public record CountIngredient(Ingredient base, int count) implements ICustomIngredient {
    public static final MapCodec<CountIngredient> CODEC = RecordCodecBuilder.mapCodec(b -> b.group(
                    Ingredient.CODEC.fieldOf("base").forGetter(CountIngredient::getBase),
                    Codec.INT.fieldOf("count").forGetter(CountIngredient::getCount))
            .apply(b, CountIngredient::new));

    @Override
    public boolean test(ItemStack stack) {
        return stack.getCount() == count && base.test(stack);
    }

    @Override
    public Stream<ItemStack> getItems() {
        return Arrays.stream(base.getItems());
    }

    @Override
    public boolean isSimple() {
        return base.isSimple();
    }

    @Override
    public IngredientType<?> getType() {
        return AllIngredientTypes.COUNT_INGREDIENT_TYPE.get();
    }

    public Ingredient getBase() {
        return base;
    }

    public int getCount() {
        return count;
    }
}
