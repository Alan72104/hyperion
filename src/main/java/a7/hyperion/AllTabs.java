package a7.hyperion;

import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.world.item.CreativeModeTab;

import static a7.hyperion.Hyperion.REGISTRATE;

public class AllTabs {
    public static final RegistryEntry<CreativeModeTab, CreativeModeTab> HYPERION = REGISTRATE.defaultCreativeTab("hyperion")
            .register();

    public static void register() {
    }
}
