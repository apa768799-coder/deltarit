package com.zeus.client.modules.movement;

import com.zeus.client.utils.Category;
import com.zeus.client.utils.Module;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

public class Jesus extends Module {
    public Jesus() { super("Jesus", Category.MOVEMENT, GLFW.GLFW_KEY_UNKNOWN); }

    @Override
    public void onTick() {
        if (mc.player == null) return;
        if (mc.player.isTouchingWater() && !mc.player.isSneaking()) {
            Vec3d v = mc.player.getVelocity();
            mc.player.setVelocity(v.x, 0.11, v.z);
        }
    }
}
