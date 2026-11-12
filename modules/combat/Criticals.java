package com.zeus.client.modules.combat;

import com.zeus.client.utils.Category;
import com.zeus.client.utils.Module;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import org.lwjgl.glfw.GLFW;

public class Criticals extends Module {
    public Criticals() { super("Criticals", Category.COMBAT, GLFW.GLFW_KEY_UNKNOWN); }

    @Override
    public void onTick() {
        if (mc.player == null || mc.networkHandler == null) return;
        if (!mc.player.handSwinging || !mc.player.isOnGround()) return;
        double x = mc.player.getX();
        double y = mc.player.getY();
        double z = mc.player.getZ();
        mc.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(x, y + 0.11, z, false, false));
        mc.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(x, y + 0.1100013579, z, false, false));
        mc.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(x, y, z, false, false));
    }
}
