package com.zeus.client.modules.movement;

import com.zeus.client.utils.Category;
import com.zeus.client.utils.Module;
import org.lwjgl.glfw.GLFW;

public class Fly extends Module {
    public Fly() { super("Fly", Category.MOVEMENT, GLFW.GLFW_KEY_F); }

    @Override
    public void onTick() {
        if (mc.player == null || mc.currentScreen != null) return;
        mc.player.getAbilities().flying = true;
        mc.player.getAbilities().setFlySpeed(0.05f);
    }

    @Override
    public void onDisable() {
        if (mc.player != null) mc.player.getAbilities().flying = false;
    }
}
