package com.zeus.client.modules.movement;

import com.zeus.client.utils.Category;
import com.zeus.client.utils.Module;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

public class WindHop extends Module {
    public WindHop() { super("WindHop", Category.MOVEMENT, GLFW.GLFW_KEY_UNKNOWN); }

    @Override
    public void onTick() {
        if (mc.player == null) return;
        Vec3d v = mc.player.getVelocity();
        if (!mc.player.isOnGround() && v.y > 0.42) {
            mc.player.setVelocity(v.x, Math.min(v.y * 1.35, 1.6), v.z);
        }
    }
}
