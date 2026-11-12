package com.zeus.client.modules.combat;

import com.zeus.client.utils.Category;
import com.zeus.client.utils.Module;
import com.zeus.client.utils.TeamUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.glfw.GLFW;

import java.util.List;

public class AimAssist extends Module {
    public AimAssist() { super("AimAssist", Category.COMBAT, GLFW.GLFW_KEY_UNKNOWN); }

    @Override
    public void onTick() {
        if (mc.player == null || mc.world == null || mc.currentScreen != null) return;
        List<LivingEntity> targets = mc.world.getEntitiesByClass(LivingEntity.class,
                mc.player.getBoundingBox().expand(6.0),
                e -> e instanceof PlayerEntity && e != mc.player && !e.isDead() && !TeamUtils.isAlly(mc, e));
        if (targets.isEmpty()) return;
        targets.sort((a, b) -> Float.compare(mc.player.distanceTo(a), mc.player.distanceTo(b)));
        LivingEntity target = targets.get(0);
        double dx = target.getX() - mc.player.getX();
        double dz = target.getZ() - mc.player.getZ();
        double dy = (target.getY() + target.getStandingEyeHeight()) - (mc.player.getY() + mc.player.getStandingEyeHeight());
        double dist = Math.sqrt(dx * dx + dz * dz);
        float targetYaw = (float) (Math.atan2(dz, dx) * 180.0 / Math.PI) - 90.0f;
        float targetPitch = (float) (-Math.atan2(dy, dist) * 180.0 / Math.PI);
        float deltaYaw = wrap(targetYaw - mc.player.getYaw());
        mc.player.setYaw(mc.player.getYaw() + deltaYaw * 0.4f);
        mc.player.setPitch(MathHelper.clamp(mc.player.getPitch() + (targetPitch - mc.player.getPitch()) * 0.4f, -90.0f, 90.0f));
    }

    private float wrap(float deg) {
        while (deg > 180.0f) deg -= 360.0f;
        while (deg < -180.0f) deg += 360.0f;
        return deg;
    }
}
