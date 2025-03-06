package a7.hyperion;

import a7.hyperion.data.CountIngredient;
import net.neoforged.neoforge.common.crafting.IngredientType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import static a7.hyperion.Hyperion.MOD_ID;

public class AllIngredientTypes {
    private static final DeferredRegister<IngredientType<?>> REGISTRAR = DeferredRegister.create(NeoForgeRegistries.Keys.INGREDIENT_TYPES, MOD_ID);

    public static final DeferredHolder<IngredientType<?>, IngredientType<CountIngredient>> COUNT_INGREDIENT_TYPE =
            REGISTRAR.register("count_ingredient_type", () -> new IngredientType<>(CountIngredient.CODEC));
}
