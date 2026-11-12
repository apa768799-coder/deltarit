package com.zeus.client.utils;

import com.zeus.client.gui.ClickGuiScreen;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;

public class BindManager {
    public static int openGuiKey = GLFW.GLFW_KEY_RIGHT_SHIFT;
    public static Module listeningModule = null;
    private static boolean prevOpenGui = false;

    public static void update(MinecraftClient client) {
        if (client == null || client.getWindow() == null) return;
        long window = client.getWindow().getHandle();
        boolean openGuiPressed = isKeyPressed(window, openGuiKey);
        if (openGuiPressed && !prevOpenGui) {
            if (client.currentScreen == null) client.setScreen(new ClickGuiScreen());
            else if (client.currentScreen instanceof ClickGuiScreen) client.setScreen(null);
        }
        prevOpenGui = openGuiPressed;
        if (client.currentScreen == null && listeningModule == null) {
            for (Module m : ModuleManager.modules) {
                if (m.getKey() != GLFW.GLFW_KEY_UNKNOWN && isKeyPressed(window, m.getKey())) {
                    if (!m.isKeyPressedLastTick()) m.toggle();
                    m.setKeyPressedLastTick(true);
                } else {
                    m.setKeyPressedLastTick(false);
                }
            }
        }
    }

    private static boolean isKeyPressed(long window, int key) {
        if (key == GLFW.GLFW_KEY_UNKNOWN) return false;
        return GLFW.glfwGetKey(window, key) == GLFW.GLFW_PRESS;
    }

    public static String getKeyName(int key) {
        if (key == GLFW.GLFW_KEY_UNKNOWN) return "NONE";
        String name = GLFW.glfwGetKeyName(key, GLFW.glfwGetKeyScancode(key));
        return name != null ? name.toUpperCase() : "KEY " + key;
    }
}
