package com.zeus.client.mixin;

import com.zeus.client.modules.player.Timer;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RenderTickCounter.Dynamic.class)
public class RenderTickCounterDynamicMixin {
    @Shadow
    private float tickDeltaPerMs;

    @Inject(method = "beginTick", at = @At("HEAD"))
    private void zeus$beginTick(long timeMillis, CallbackInfoReturnable<Integer> cir) {
        float speed = Timer.speed();
        tickDeltaPerMs = 0.02f * (speed > 0.0f ? speed : 1.0f);
    }
}
