package com.zeus.client.mixin;

import com.zeus.client.modules.combat.Hitbox;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin {
    @Inject(method = "getBoundingBox", at = @At("RETURN"), cancellable = true)
    private void zeus$boundingBox(CallbackInfoReturnable<Box> cir) {
        Object self = this;
        if (!(self instanceof PlayerEntity)) return;
        if (self == MinecraftClient.getInstance().player) return;
        double expand = Hitbox.expandFor((Entity) self);
        if (expand <= 0.0) return;
        cir.setReturnValue(cir.getReturnValue().expand(expand, 0.0, expand));
    }
}
