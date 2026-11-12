package com.zeus.client.modules.combat;

import com.zeus.client.utils.Category;
import com.zeus.client.utils.Module;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

public class Velocity extends Module {
    public Velocity() { super("Velocity", Category.COMBAT, GLFW.GLFW_KEY_UNKNOWN); }

    @Override
    public void onTick() {
        if (mc.player == null) return;
        if (mc.player.hurtTime >= 9) {
            Vec3d v = mc.player.getVelocity();
            mc.player.setVelocity(0.0, v.y, 0.0);
        }
    }
}
