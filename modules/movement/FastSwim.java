package com.zeus.client.modules.movement;

import com.zeus.client.utils.Category;
import com.zeus.client.utils.Module;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

public class FastSwim extends Module {
    public FastSwim() { super("FastSwim", Category.MOVEMENT, GLFW.GLFW_KEY_UNKNOWN); }

    @Override
    public void onTick() {
        if (mc.player == null) return;
        if (!mc.player.isTouchingWater()) return;
        Vec3d v = mc.player.getVelocity();
        double hx = v.x * 1.25;
        double hz = v.z * 1.25;
        double h = Math.hypot(hx, hz);
        double cap = 0.35;
        if (h > cap) { hx = hx / h * cap; hz = hz / h * cap; }
        double vy = mc.player.input.playerInput.jump() ? 0.25 : v.y * 1.1;
        mc.player.setVelocity(hx, vy, hz);
    }
}
