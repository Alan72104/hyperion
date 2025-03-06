package a7.hyperion.item;

import a7.hyperion.render.debug.MyDebugRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.neoforged.fml.loading.FMLEnvironment;

import java.util.Optional;

public interface ITeleportTool {
    float getTeleportDistance();

    default Vec3 teleportForward(Level level, Player player) {
        final float dist = getTeleportDistance();
        Vec3 eye = player.getEyePosition();
        Vec3 target = eye.add(player.getViewVector(0).scale(dist));
        EntityDimensions dims = player.getDimensions(player.getPose());
        BlockHitResult hitResult = level.clip(new ClipContext(eye, target, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player));
        Vec3 hitPos = hitResult.getLocation();
        if (hitResult.getType() == HitResult.Type.BLOCK && !level.noCollision(dims.makeBoundingBox(hitPos))) {
            AABB searchSpace = AABB.ofSize(
                    hitPos,
                    dims.width() + 1.0E-6, dims.height() + 1.0E-6, dims.width() + 1.0E-6);
            Optional<Vec3> free = level.findFreePosition(player,
                    Shapes.create(searchSpace),
                    eye,
                    dims.width(), dims.height(), dims.width());
            if (free.isEmpty())
                player.sendSystemMessage(Component.literal("free pos not found"));
            else
                hitPos = free.get().add(0, -dims.height() / 2, 0);
            if (FMLEnvironment.dist.isClient())
                MyDebugRenderer.INSTANCE.setTeleportation(new MyDebugRenderer.Teleportation(player.position(), player.getDimensions(player.getPose()), searchSpace, player.getViewVector(0), dist, hitPos));
        }
        player.teleportTo(hitPos.x, hitPos.y, hitPos.z);
        return hitPos;
    }
}
