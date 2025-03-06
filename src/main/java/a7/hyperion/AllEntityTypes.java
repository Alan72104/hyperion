package a7.hyperion;

import a7.hyperion.entity.TerminatorArrow;
import com.tterrag.registrate.builders.EntityBuilder;
import com.tterrag.registrate.util.entry.EntityEntry;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

import static a7.hyperion.Hyperion.REGISTRATE;

public class AllEntityTypes {
    public static final EntityEntry<TerminatorArrow> TERMINATOR_ARROW = register(
            "terminator_arrow",
            TerminatorArrow::new,
            () -> ctx -> new ArrowRenderer<TerminatorArrow>(ctx) {
                public static final ResourceLocation NORMAL_ARROW_LOCATION = ResourceLocation.withDefaultNamespace("textures/entity/projectiles/arrow.png");
                public static final ResourceLocation TIPPED_ARROW_LOCATION = ResourceLocation.withDefaultNamespace("textures/entity/projectiles/tipped_arrow.png");

                @Override
                public ResourceLocation getTextureLocation(TerminatorArrow entity) {
                    return entity.getColor() > 0 ? TIPPED_ARROW_LOCATION : NORMAL_ARROW_LOCATION;
                }
            },
            MobCategory.MISC)
            .register();

    private static <T extends Entity> EntityBuilder<T, ?> register(String name,
                                                                   EntityType.EntityFactory<T> factory,
                                                                   NonNullSupplier<NonNullFunction<EntityRendererProvider.Context, EntityRenderer<? super T>>> renderer,
                                                                   MobCategory group) {
        return REGISTRATE
                .entity(name, factory, group)
                .renderer(renderer);
    }

    public static void register() {
    }
}
