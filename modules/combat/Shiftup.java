package com.zeus.client.modules.combat;

import com.zeus.client.utils.Category;
import com.zeus.client.utils.Module;
import org.lwjgl.glfw.GLFW;

public class Shiftup extends Module {
    public Shiftup() { super("Shiftup", Category.COMBAT, GLFW.GLFW_KEY_UNKNOWN); }

    @Override
    public void onTick() {
        if (mc.player == null) return;
        if (mc.player.hurtTime >= 8) mc.player.setSneaking(true);
        else if (!mc.options.sneakKey.isPressed()) mc.player.setSneaking(false);
    }

    @Override
    public void onDisable() {
        if (mc.player != null) mc.player.setSneaking(false);
    }
}
