package a7.hyperion;

import a7.hyperion.dungeon.DungeonManager;
import a7.hyperion.render.debug.MyDebugRenderer;
import com.mojang.datafixers.util.Either;
import it.unimi.dsi.fastutil.objects.Reference2ObjectMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.client.event.RenderTooltipEvent;

@Mod(value = Hyperion.MOD_ID, dist = Dist.CLIENT)
public class HyperionClient {
    public static final Reference2ObjectMap<Item, String[]> TOOLTIPS = new Reference2ObjectOpenHashMap<>();

    public static final DungeonManager DUNGEONS = new DungeonManager();

    public HyperionClient(IEventBus modEventBus, ModContainer modContainer) {
    }

    @EventBusSubscriber(modid = Hyperion.MOD_ID, value = Dist.CLIENT)
    static class ClientEvents {
        @SubscribeEvent
        static void tickPost(ClientTickEvent.Post event) {

        }
        @SubscribeEvent
        static void interactionKeyMappingTriggered(InputEvent.InteractionKeyMappingTriggered event) {
            if (!event.isUseItem())
                return;
            var mc = Minecraft.getInstance();
            if (mc.player == null || mc.level == null)
                return;
            ItemStack itemstack = Minecraft.getInstance().player.getItemInHand(event.getHand());
            if (!itemstack.isItemEnabled(Minecraft.getInstance().level.enabledFeatures())) {
                return;
            }
        }

        @SubscribeEvent
        static void renderLevelStage(RenderLevelStageEvent event) {
            if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_WEATHER) {
                MyDebugRenderer.INSTANCE.render(event.getPoseStack(),
                        Minecraft.getInstance().renderBuffers().bufferSource(),
                        event.getCamera().getPosition().x,
                        event.getCamera().getPosition().y,
                        event.getCamera().getPosition().z);
            }
        }

        @SubscribeEvent()
        static void gatherTooltip(RenderTooltipEvent.GatherComponents event) {
            Item item = event.getItemStack().getItem();
            String[] tooltip = TOOLTIPS.get(item);
            if (tooltip == null)
                return;
            for (String s : tooltip)
                event.getTooltipElements().add(Either.left(FormattedText.of(s)));
        }
    }

    @EventBusSubscriber(modid = Hyperion.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    static class ClientModEvents {
        @SubscribeEvent
        static void clientSetup(FMLClientSetupEvent event) {
            Hyperion.LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }
    }
}
