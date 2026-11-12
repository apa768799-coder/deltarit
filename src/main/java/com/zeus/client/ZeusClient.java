package com.zeus.client;

import com.zeus.client.modules.render.BlockESP;
import com.zeus.client.modules.render.HUD;
import com.zeus.client.utils.BindManager;
import com.zeus.client.utils.Friends;
import com.zeus.client.utils.Module;
import com.zeus.client.utils.ModuleManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;

public class ZeusClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModuleManager.init();
        Friends.load();
        Module.notifier = HUD::notify;

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            BindManager.update(client);
            if (client.player == null || client.world == null) return;
            for (var m : ModuleManager.modules) {
                if (!m.isEnabled()) continue;
                if (client.currentScreen != null && !m.runsInScreens()) continue;
                m.onTick();
            }
        });

        WorldRenderEvents.AFTER_ENTITIES.register(context -> {
            for (var m : ModuleManager.modules) {
                if (m.isEnabled()) m.onRender3D(context);
            }
        });

        HudRenderCallback.EVENT.register((context, tickCounter) -> {
            if (ModuleManager.isEnabled("HUD")) HUD.render(context);
            Module blockEsp = ModuleManager.get("BlockESP");
            if (blockEsp != null && blockEsp.isEnabled()) {
                BlockESP.render(context, (BlockESP) blockEsp);
            }
        });
    }
}
