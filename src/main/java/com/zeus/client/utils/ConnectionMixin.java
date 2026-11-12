package com.zeus.client.mixin;

import com.zeus.client.utils.PacketHooks;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.packet.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientConnection.class)
public class ClientConnectionMixin {
    @Inject(method = "send", at = @At("HEAD"), cancellable = true)
    private void zeus$send(Packet<?> packet, PacketListener listener, CallbackInfo ci) {
        if (PacketHooks.onSend(packet)) ci.cancel();
    }

    @Inject(method = "handlePacket", at = @At("HEAD"), cancellable = true)
    private static void zeus$handlePacket(Packet<?> packet, PacketListener listener, CallbackInfo ci) {
        if (PacketHooks.onReceive(packet)) ci.cancel();
    }
}
