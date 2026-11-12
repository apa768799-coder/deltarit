package com.zeus.client.modules.movement;

import com.zeus.client.utils.Category;
import com.zeus.client.utils.Module;
import org.lwjgl.glfw.GLFW;

public class LongJump extends Module {
    private int cooldown = 0;

    public LongJump() { super("LongJump", Category.MOVEMENT, GLFW.GLFW_KEY_UNKNOWN); }

    @Override
    public void onTick() {
        if (mc.player == null) return;
        if (cooldown > 0) { cooldown--; return; }
        boolean moving = mc.player.input.playerInput.forward() || mc.player.input.playerInput.left()
                || mc.player.input.playerInput.right() || mc.player.input.playerInput.backward();
        if (!mc.player.isOnGround() || !moving) return;
        double rad = Math.toRadians(mc.player.getYaw());
        mc.player.setVelocity(-Math.sin(rad) * 0.7, 0.42, Math.cos(rad) * 0.7);
        mc.player.setSprinting(true);
        cooldown = 20;
    }
}
