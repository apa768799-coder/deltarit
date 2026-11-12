package com.zeus.client.modules.player;

import com.zeus.client.utils.Category;
import com.zeus.client.utils.Module;
import net.minecraft.block.BedBlock;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

public class BedAura extends Module {
    private BlockPos breakingPos = null;

    public BedAura() { super("BedAura", Category.PLAYER, GLFW.GLFW_KEY_UNKNOWN); }

    @Override
    public void onTick() {
        if (mc.player == null || mc.world == null || mc.interactionManager == null || mc.currentScreen != null) return;
        BlockPos bed = findBed();
        if (bed == null) { breakingPos = null; return; }
        face(bed);
        if (breakingPos == null || !breakingPos.equals(bed)) {
            breakingPos = bed;
            mc.interactionManager.attackBlock(bed, Direction.UP);
        } else {
            mc.interactionManager.updateBlockBreakingProgress(bed, Direction.UP);
        }
        mc.player.swingHand(Hand.MAIN_HAND);
    }

    private BlockPos findBed() {
        BlockPos origin = mc.player.getBlockPos();
        for (int dx = -5; dx <= 5; dx++) {
            for (int dy = -3; dy <= 3; dy++) {
                for (int dz = -5; dz <= 5; dz++) {
                    BlockPos pos = origin.add(dx, dy, dz);
                    if (mc.world.getBlockState(pos).getBlock() instanceof BedBlock) return pos;
                }
            }
        }
        return null;
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
