package a7.hyperion;

import a7.hyperion.data.MyItemModelProvider;
import a7.hyperion.data.MyRecipeProvider;
import a7.hyperion.dungeon.DungeonManager;
import a7.hyperion.payload.GenerateDungeonPayload;
import com.mojang.logging.LogUtils;
import com.tterrag.registrate.Registrate;
import it.unimi.dsi.fastutil.objects.Reference2ObjectMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.profiling.jfr.event.PacketReceivedEvent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.RenderTooltipEvent;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.TagsUpdatedEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.registries.holdersets.OrHolderSet;
import org.slf4j.Logger;

import java.lang.reflect.Field;
import java.util.concurrent.CompletableFuture;

@Mod(Hyperion.MOD_ID)
public class Hyperion {
    public static final String MOD_ID = "hyperion";
    public static final Logger LOGGER = LogUtils.getLogger();
    @SuppressWarnings("NotNullFieldNotInitialized")
    public static Registrate REGISTRATE;

    public static final DungeonManager DUNGEONS = new DungeonManager();

    public Hyperion(IEventBus modEventBus, ModContainer modContainer) {
        REGISTRATE = Registrate.create(MOD_ID);
        AllTabs.register();
        AllBlocks.register();
        AllFluids.register();
        AllItems.register();
        AllEntityTypes.register();

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    public static ResourceLocation loc(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    @EventBusSubscriber(modid = MOD_ID)
    static class Events {
        @SubscribeEvent(priority = EventPriority.LOWEST)
        static void tagsUpdated(TagsUpdatedEvent event) {
            var regAccess = event.getRegistryAccess();
            addSupportedItems(regAccess, Enchantments.MULTISHOT, AllItems.TERMINATOR);
            addSupportedItems(regAccess, Enchantments.INFINITY, AllItems.TERMINATOR);
            addSupportedItems(regAccess, Enchantments.PIERCING, AllItems.TERMINATOR);
        }

        @SafeVarargs
        static void addSupportedItems(RegistryAccess registryAccess, ResourceKey<Enchantment> key, Holder<Item>... items) {
            var enchantmentReg = registryAccess.lookupOrThrow(Registries.ENCHANTMENT);
            Holder.Reference<Enchantment> enchantment = enchantmentReg.get(key).orElseThrow();
            HolderSet<Item> supportedItems = enchantment.value().definition().supportedItems();
            HolderSet<Item> newSupportedItems = HolderSet.direct(items);
            try {
                Field field = Enchantment.EnchantmentDefinition.class.getDeclaredField("supportedItems");
                field.set(enchantment.value().definition(), new OrHolderSet<>(supportedItems, newSupportedItems));
            } catch (NoSuchFieldException | IllegalAccessException e) {
                LOGGER.error("Error adding supported enchantments to {}", key);
                LOGGER.error("", e);
            }
        }
    }

    @EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.MOD)
    static class ModEvents {
        @SubscribeEvent
        static void commonSetup(final FMLCommonSetupEvent event) {
            if (Config.logDirtBlock)
                LOGGER.info("DIRT BLOCK >> {}", BuiltInRegistries.BLOCK.getKey(Blocks.DIRT));

            LOGGER.info("{}{}", Config.magicNumberIntroduction, Config.magicNumber);

            Config.items.forEach((item) -> LOGGER.info("ITEM >> {}", item.toString()));
        }

        @SubscribeEvent
        static void gatherData(GatherDataEvent event) {
            DataGenerator generator = event.getGenerator();
            PackOutput output = generator.getPackOutput();
            ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
            CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

            generator.addProvider(event.includeServer(), new MyRecipeProvider(output, lookupProvider));
            generator.addProvider(event.includeClient(), new MyItemModelProvider(output, MOD_ID, existingFileHelper));
        }

        @SubscribeEvent
        static void addCreative(BuildCreativeModeTabContentsEvent event) {
        }

        @SubscribeEvent
        static void registerPayloadHandlers(RegisterPayloadHandlersEvent event) {
            AllPackets.register(event);
        }
    }
}
