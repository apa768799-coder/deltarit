package com.zeus.client.modules.render;

import com.zeus.client.utils.Category;
import com.zeus.client.utils.Module;
import com.zeus.client.utils.TeamUtils;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import org.lwjgl.glfw.GLFW;

public class ESP extends Module {
    public ESP() { super("ESP", Category.RENDER, GLFW.GLFW_KEY_UNKNOWN); }

    @Override
    public void onTick() {
        if (mc.world == null || mc.player == null) return;
        for (AbstractClientPlayerEntity p : mc.world.getPlayers()) {
            if (p == mc.player) continue;
            p.setGlowing(!TeamUtils.isAlly(mc, p));
        }
    }

    @Override
    public void onDisable() {
        if (mc.world == null) return;
        for (AbstractClientPlayerEntity p : mc.world.getPlayers()) p.setGlowing(false);
    }
}
