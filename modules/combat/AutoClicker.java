package com.zeus.client.modules.combat;

import com.zeus.client.utils.Category;
import com.zeus.client.utils.Module;
import net.minecraft.client.option.KeyBinding;
import org.lwjgl.glfw.GLFW;

import java.util.Random;

public class AutoClicker extends Module {
    private static final Random RANDOM = new Random();
    private int delay = 0;

    public AutoClicker() { super("AutoClicker", Category.COMBAT, GLFW.GLFW_KEY_UNKNOWN); }

    @Override
    public void onTick() {
        if (mc.player == null || mc.currentScreen != null) return;
        if (delay > 0) { delay--; return; }
        KeyBinding.onKeyPressed(mc.options.attackKey.getDefaultKey());
        double cps = 9.0 + RANDOM.nextDouble() * 4.0;
        delay = Math.max(1, (int) Math.floor(20.0 / cps));
    }
}
