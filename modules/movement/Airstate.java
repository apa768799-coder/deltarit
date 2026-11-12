package com.zeus.client.modules.movement;

import com.zeus.client.utils.Category;
import com.zeus.client.utils.Module;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

public class Airstate extends Module {
    public Airstate() { super("Airstate", Category.MOVEMENT, GLFW.GLFW_KEY_UNKNOWN); }

    @Override
    public void onTick() {
        if (mc.player == null) return;
        if (!mc.player.isOnGround()) {
            Vec3d v = mc.player.getVelocity();
            if (v.y < 0) mc.player.setVelocity(v.x, 0.0, v.z);
        }
    }
}
