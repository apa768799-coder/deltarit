package com.zeus.client.modules.render;

import com.zeus.client.utils.Category;
import com.zeus.client.utils.Module;
import org.lwjgl.glfw.GLFW;

public class FullBright extends Module {
    private double prevGamma = 1.0;

    public FullBright() { super("FullBright", Category.RENDER, GLFW.GLFW_KEY_UNKNOWN); }

    @Override
    public void onEnable() {
        prevGamma = mc.options.gamma.getValue();
        mc.options.gamma.setValue(1.0);
    }

    @Override
    public void onDisable() {
        mc.options.gamma.setValue(prevGamma);
    }
}
