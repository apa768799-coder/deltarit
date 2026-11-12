package com.zeus.client.modules.movement;

import com.zeus.client.utils.Category;
import com.zeus.client.utils.Module;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

public class NoSlow extends Module {
    public NoSlow() { super("NoSlow", Category.MOVEMENT, GLFW.GLFW_KEY_UNKNOWN); }

    @Override
    public void onTick() {
        if (mc.player == null) return;
        if (!mc.player.isUsingItem()) return;
        boolean moving = mc.player.input.playerInput.forward() || mc.player.input.playerInput.left()
                || mc.player.input.playerInput.right() || mc.player.input.playerInput.backward();
        if (!moving) return;
        Vec3d v = mc.player.getVelocity();
        if (Math.hypot(v.x, v.z) < 0.286) mc.player.setVelocity(v.x * 2.2, v.y, v.z * 2.2);
    }
}
