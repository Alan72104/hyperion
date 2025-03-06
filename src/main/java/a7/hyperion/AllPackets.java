package a7.hyperion;

import a7.hyperion.payload.GenerateDungeonPayload;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class AllPackets {
    public static void register(RegisterPayloadHandlersEvent event) {
        // Sets the current network version
        final PayloadRegistrar registrar = event.registrar("1");
        registrar.playToServer(
                GenerateDungeonPayload.TYPE,
                GenerateDungeonPayload.STREAM_CODEC,
                GenerateDungeonPayload::handle
        );
    }
}
