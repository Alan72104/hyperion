package a7.hyperion.payload;

import a7.hyperion.Hyperion;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record GenerateDungeonPayload() implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<GenerateDungeonPayload> TYPE = new CustomPacketPayload.Type<>(Hyperion.loc("generate_dungeon"));
    public static final GenerateDungeonPayload INSTANCE = new GenerateDungeonPayload();
    public static final StreamCodec<ByteBuf, GenerateDungeonPayload> STREAM_CODEC = StreamCodec.unit(
            INSTANCE
    );

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handle(IPayloadContext ctx) {
        Hyperion.DUNGEONS.generateDungeon();
    }
}
