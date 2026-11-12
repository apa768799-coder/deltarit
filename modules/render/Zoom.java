package com.zeus.client.modules.render;

import com.zeus.client.utils.Category;
import com.zeus.client.utils.Module;
import org.lwjgl.glfw.GLFW;

public class Zoom extends Module {
    private int prevFov = 100;

    public Zoom() { super("Zoom", Category.RENDER, GLFW.GLFW_KEY_UNKNOWN); }

    @Override
    public void onEnable() {
        prevFov = mc.options.fov.getValue();
        mc.options.fov.setValue(10);
    }

    @Override
    public void onDisable() {
        mc.options.fov.setValue(prevFov);
    }
}
