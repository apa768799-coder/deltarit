package com.zeus.client.modules.movement;

import com.zeus.client.utils.Category;
import com.zeus.client.utils.Module;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

public class SafeWalk extends Module {
    public SafeWalk() { super("SafeWalk", Category.MOVEMENT, GLFW.GLFW_KEY_UNKNOWN); }

    @Override
    public void onTick() {
        if (mc.player == null || mc.world == null) return;
        if (!mc.player.isOnGround()) { mc.player.setSneaking(false); return; }
        Vec3d v = mc.player.getVelocity();
        int dx = v.x > 0.05 ? 1 : (v.x < -0.05 ? -1 : 0);
        int dz = v.z > 0.05 ? 1 : (v.z < -0.05 ? -1 : 0);
        BlockPos base = mc.player.getBlockPos().down();
        BlockPos ahead = base.add(dx, 0, dz);
        boolean danger = mc.world.getBlockState(ahead).isAir() && !mc.world.getBlockState(base).isAir();
        mc.player.setSneaking(danger);
    }

    @Override
    public void onDisable() {
        if (mc.player != null) mc.player.setSneaking(false);
    }
}
