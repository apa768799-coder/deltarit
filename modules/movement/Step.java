package com.zeus.client.modules.movement;

import com.zeus.client.utils.Category;
import com.zeus.client.utils.Module;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

public class Step extends Module {
    public Step() { super("Step", Category.MOVEMENT, GLFW.GLFW_KEY_UNKNOWN); }

    @Override
    public void onTick() {
        if (mc.player == null) return;
        boolean moving = mc.player.input.playerInput.forward() || mc.player.input.playerInput.left()
                || mc.player.input.playerInput.right() || mc.player.input.playerInput.backward();
        if (mc.player.horizontalCollision && mc.player.isOnGround() && moving) {
            Vec3d v = mc.player.getVelocity();
            mc.player.setVelocity(v.x, 0.42, v.z);
        }
    }
}
