package com.zeus.client.modules.movement;

import com.zeus.client.utils.Category;
import com.zeus.client.utils.Module;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

public class WaterJump extends Module {
    public WaterJump() { super("WaterJump", Category.MOVEMENT, GLFW.GLFW_KEY_UNKNOWN); }

    @Override
    public void onTick() {
        if (mc.player == null) return;
        if (mc.player.isTouchingWater() && mc.options.jumpKey.isPressed()) {
            Vec3d v = mc.player.getVelocity();
            mc.player.setVelocity(v.x, 0.35, v.z);
        }
    }
}
