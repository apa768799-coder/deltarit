package com.zeus.client.mixin;

import com.zeus.client.modules.render.FreeCam;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public class CameraMixin {
    @Shadow
    protected void setPos(Vec3d pos) {
    }

    @Inject(method = "update", at = @At("TAIL"))
    private void zeus$update(BlockView area, Entity focusedEntity, boolean thirdPerson,
                             boolean inverseView, float tickDelta, CallbackInfo ci) {
        Vec3d override = FreeCam.cameraPos();
        if (override != null) setPos(override);
    }
}
