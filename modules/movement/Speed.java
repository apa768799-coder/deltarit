package com.zeus.client.modules.movement;

import com.zeus.client.utils.Category;
import com.zeus.client.utils.Module;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

public class Speed extends Module {
    public Speed() { super("Speed", Category.MOVEMENT, GLFW.GLFW_KEY_V); }

    @Override
    public void onTick() {
        if (mc.player == null || mc.currentScreen != null) return;
        if (mc.player.horizontalCollision) return;
        Vec3d vel = mc.player.getVelocity();
        if (mc.player.input.playerInput.forward() || mc.player.input.playerInput.backward()
                || mc.player.input.playerInput.left() || mc.player.input.playerInput.right()) {
            if (vel.y < 0.1 && vel.y > -0.1) {
                mc.player.setVelocity(vel.x * 1.2, vel.y, vel.z * 1.2);
            }
        }
    }
}
