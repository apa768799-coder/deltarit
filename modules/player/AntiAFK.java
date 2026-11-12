package com.zeus.client.modules.player;

import com.zeus.client.utils.Category;
import com.zeus.client.utils.Module;
import net.minecraft.client.option.KeyBinding;
import org.lwjgl.glfw.GLFW;

public class AntiAFK extends Module {
    private int ticks = 0;

    public AntiAFK() { super("AntiAFK", Category.PLAYER, GLFW.GLFW_KEY_UNKNOWN); }

    @Override
    public void onTick() {
        if (mc.player == null) return;
        ticks++;
        if (ticks % 60 == 0) mc.player.setYaw(mc.player.getYaw() + 90.0f);
        if (ticks % 120 == 0) KeyBinding.onKeyPressed(mc.options.jumpKey.getDefaultKey());
    }
}
