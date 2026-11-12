package com.zeus.client.modules.player;

import com.zeus.client.utils.Category;
import com.zeus.client.utils.Module;
import net.minecraft.client.option.KeyBinding;
import org.lwjgl.glfw.GLFW;

public class FastPlace extends Module {
    public FastPlace() { super("FastPlace", Category.PLAYER, GLFW.GLFW_KEY_UNKNOWN); }

    @Override
    public void onTick() {
        if (mc.player == null || mc.currentScreen != null) return;
        KeyBinding.onKeyPressed(mc.options.useKey.getDefaultKey());
    }
}
