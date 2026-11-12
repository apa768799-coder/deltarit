package com.zeus.client.modules.player;

import com.zeus.client.utils.Category;
import com.zeus.client.utils.Module;
import net.minecraft.client.gui.screen.DeathScreen;
import org.lwjgl.glfw.GLFW;

public class AutoRespawn extends Module {
    public AutoRespawn() { super("AutoRespawn", Category.PLAYER, GLFW.GLFW_KEY_UNKNOWN); }

    @Override
    public boolean runsInScreens() { return true; }

    @Override
    public void onTick() {
        if (mc.player == null) return;
        if (mc.currentScreen instanceof DeathScreen) {
            mc.player.requestRespawn();
            mc.setScreen(null);
        }
    }
}
