package com.zeus.client.modules.movement;

import com.zeus.client.utils.Category;
import com.zeus.client.utils.Module;
import org.lwjgl.glfw.GLFW;

public class BunnyHop extends Module {
    public BunnyHop() { super("BunnyHop", Category.MOVEMENT, GLFW.GLFW_KEY_UNKNOWN); }

    @Override
    public void onTick() {
        if (mc.player == null) return;
        boolean moving = mc.player.input.playerInput.forward() || mc.player.input.playerInput.left()
                || mc.player.input.playerInput.right() || mc.player.input.playerInput.backward();
        mc.options.jumpKey.setPressed(mc.player.isOnGround() && moving);
    }

    @Override
    public void onDisable() {
        if (mc.player != null) mc.options.jumpKey.setPressed(false);
    }
}
