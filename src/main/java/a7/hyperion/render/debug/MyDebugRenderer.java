package a7.hyperion.render.debug;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.Util;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.debug.DebugRenderer;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
public class MyDebugRenderer {
    public static final MyDebugRenderer INSTANCE = new MyDebugRenderer();
    private long lastTpTime = Long.MIN_VALUE;
    @Nullable
    private Teleportation tp;

    private MyDebugRenderer() {
    }

    private static void renderVector(PoseStack poseStack, MultiBufferSource bufferSource, Vec3 pos, Vec3 vector) {
        PoseStack.Pose pose = poseStack.last();
        VertexConsumer buffer = bufferSource.getBuffer(RenderType.lines());
        buffer.addVertex(pose, (float) pos.x, (float) pos.y, (float) pos.z)
                .setColor(0xFFFFFFFF)
                .setNormal(pose, (float) vector.x, (float) vector.y, (float) vector.z);
        buffer.addVertex(pose, (float) (pos.x + vector.x), (float) (pos.y + vector.y), (float) (pos.z + vector.z))
                .setColor(0xFFFFFFFF)
                .setNormal(pose, (float) vector.x, (float) vector.y, (float) vector.z);
    }

    public void render(PoseStack ps, MultiBufferSource.BufferSource bufferSource, double camX, double camY, double camZ) {
        if (Util.getMillis() - this.lastTpTime > 30_000L) {
            tp = null;
        }

        if (tp != null) {
            ps.pushPose();
//            ps.translate(tp.pos.x - camX, tp.pos.y - camY, tp.pos.z - camZ);
//            ps.translate(tp.pos.x , tp.pos.y , tp.pos.z );
//            ps.translate(-tp.pos.x ,- tp.pos.y , -tp.pos.z );
            ps.translate(-camX, -camY, -camZ);

            // Pos
            DebugRenderer.renderFilledBox(ps, bufferSource,
                    AABB.ofSize(tp.pos, 0.1f, 0.1f, 0.1f),
                    1.0F, 1.0F, 1.0F, 1.0F);
            // To pos
            DebugRenderer.renderFilledBox(ps, bufferSource,
                    AABB.ofSize(tp.toPos, 0.1f, 0.1f, 0.1f),
                    0.0F, 1.0F, 0.0F, 1.0F);
            // Search space
            if (tp.searchSpace != null)
                LevelRenderer.renderLineBox(ps, bufferSource.getBuffer(RenderType.lines()),
                        tp.searchSpace,
                        1.0F, 1.0F, 1.0F, 1.0F);
            // Dims
            LevelRenderer.renderLineBox(ps, bufferSource.getBuffer(RenderType.lines()),
                    tp.dims.makeBoundingBox(tp.pos),
                    1.0F, 1.0F, 1.0F, 1.0F);
            // Dims at to pos
            LevelRenderer.renderLineBox(ps, bufferSource.getBuffer(RenderType.lines()),
                    tp.dims.makeBoundingBox(tp.toPos),
                    1.0F, 1.0F, 1.0F, 1.0F);
            bufferSource.endBatch();
            // Look vec
            renderVector(ps, bufferSource,
                    tp.pos.add(0, tp.dims.eyeHeight(), 0),
                    tp.view.scale(tp.dist));
            bufferSource.endBatch();
            ps.popPose();
        }
    }

    public void setTeleportation(Teleportation tp) {
        this.tp = tp;
        lastTpTime = Util.getMillis();
    }

    public record Teleportation(Vec3 pos, EntityDimensions dims, @Nullable AABB searchSpace, Vec3 view, float dist,
                                Vec3 toPos) {
    }
}

