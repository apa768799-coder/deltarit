package com.zeus.client.modules.world;

import com.zeus.client.utils.Category;
import com.zeus.client.utils.Module;
import net.minecraft.block.Blocks;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

public class Nuker extends Module {
    private BlockPos current = null;

    public Nuker() { super("Nuker", Category.WORLD, GLFW.GLFW_KEY_UNKNOWN); }

    @Override
    public void onTick() {
        if (mc.player == null || mc.world == null || mc.interactionManager == null || mc.currentScreen != null) return;
        if (current == null || mc.world.getBlockState(current).isAir()) current = findTarget();
        if (current == null) return;
        face(current);
        mc.interactionManager.updateBlockBreakingProgress(current, Direction.UP);
        mc.player.swingHand(Hand.MAIN_HAND);
    }

    @Override
    public void onEnable() {
        current = null;
    }

    private BlockPos findTarget() {
        BlockPos origin = mc.player.getBlockPos();
        BlockPos best = null;
        double bestDist = 4.0;
        for (int dx = -4; dx <= 4; dx++) {
            for (int dy = -4; dy <= 4; dy++) {
                for (int dz = -4; dz <= 4; dz++) {
                    BlockPos pos = origin.add(dx, dy, dz);
                    var state = mc.world.getBlockState(pos);
                    if (state.isAir()) continue;
                    if (state.getBlock() == Blocks.BEDROCK || state.getBlock() == Blocks.BARRIER) continue;
                    if (state.getHardness(mc.world, pos) < 0) continue;
                    double dist = Math.sqrt(pos.getSquaredDistance(mc.player.getPos()));
                    if (dist <= bestDist) { bestDist = dist; best = pos; }
                }
            }
        }
        if (best != null) mc.interactionManager.attackBlock(best, Direction.UP);
        return best;
    }

    private void face(BlockPos pos) {
        Vec3d c = Vec3d.ofCenter(pos);
        double dx = c.x - mc.player.getX();
        double dy = c.y - (mc.player.getY() + mc.player.getStandingEyeHeight());
        double dz = c.z - mc.player.getZ();
        double dist = Math.sqrt(dx * dx + dz * dz);
        mc.player.setYaw((float) (Math.atan2(dz, dx) * 180.0 / Math.PI) - 90.0f);
        mc.player.setPitch((float) (-Math.atan2(dy, dist) * 180.0 / Math.PI));
    }
}
