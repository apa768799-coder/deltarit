package com.zeus.client.modules.player;

import com.zeus.client.utils.Category;
import com.zeus.client.utils.Module;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import org.lwjgl.glfw.GLFW;

public class NoFall extends Module {
    public NoFall() { super("NoFall", Category.PLAYER, GLFW.GLFW_KEY_N); }

    @Override
    public void onTick() {
        if (mc.player == null || mc.networkHandler == null) return;
        if (mc.player.fallDistance > 3.0f && !mc.player.isOnGround()) {
            mc.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(
                    mc.player.getX(), mc.player.getY(), mc.player.getZ(), true, false));
            mc.player.fallDistance = 0.0f;
        }
    }
}
