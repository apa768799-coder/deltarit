package com.zeus.client.utils;

import com.zeus.client.modules.player.Blink;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;

public class PacketHooks {
    public static boolean onSend(Packet<?> packet) {
        return Blink.onPacketSend(packet);
    }

    public static boolean onReceive(Packet<?> packet) {
        if (packet instanceof EntityVelocityUpdateS2CPacket velocity) {
            MinecraftClient mc = MinecraftClient.getInstance();
            if (mc.player != null
                    && velocity.getId() == mc.player.getId()
                    && ModuleManager.isEnabled("Velocity")) {
                return true;
            }
        }
        return false;
    }
}
