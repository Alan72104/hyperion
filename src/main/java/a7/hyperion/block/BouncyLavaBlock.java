package a7.hyperion.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;

public class BouncyLavaBlock extends LiquidBlock {
    public BouncyLavaBlock(BaseFlowingFluid.Flowing fluid, Properties p) {
        super(fluid, p);
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        float fluidHeight = this.fluid.getHeight(state.getFluidState(), level, pos);
        if (entity.getBoundingBox().minY < pos.getY() + fluidHeight && entity.getBoundingBox().maxY > pos.getY()) {
            Vec3 d = entity.getDeltaMovement();
            entity.setDeltaMovement(new Vec3(d.x, 2.0f, d.z));
        }
    }
}
