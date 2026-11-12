package com.zeus.client.modules.movement;

import com.zeus.client.utils.Category;
import com.zeus.client.utils.Module;
import org.lwjgl.glfw.GLFW;

public class HighJump extends Module {
    public HighJump() { super("HighJump", Category.MOVEMENT, GLFW.GLFW_KEY_UNKNOWN); }

    @Override
    public void onTick() {
        if (mc.player == null) return;
        if (mc.options.jumpKey.isPressed() && mc.player.isOnGround()) {
            mc.player.setVelocity(mc.player.getVelocity().x, 0.85, mc.player.getVelocity().z);
        }
    }
}
