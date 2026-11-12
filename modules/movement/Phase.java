package com.zeus.client.modules.movement;

import com.zeus.client.utils.Category;
import com.zeus.client.utils.Module;
import org.lwjgl.glfw.GLFW;

public class Phase extends Module {
    public Phase() { super("Phase", Category.MOVEMENT, GLFW.GLFW_KEY_UNKNOWN); }

    @Override
    public void onEnable() {
        if (mc.player != null) mc.player.noClip = true;
    }

    @Override
    public void onTick() {
        if (mc.player != null) mc.player.noClip = true;
    }

    @Override
    public void onDisable() {
        if (mc.player != null) mc.player.noClip = false;
    }
}
