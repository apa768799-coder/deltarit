package com.zeus.client.modules.combat;

import com.zeus.client.utils.Category;
import com.zeus.client.utils.Module;
import com.zeus.client.utils.TeamUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Box;
import org.lwjgl.glfw.GLFW;

import java.util.List;

public class KillAura extends Module {
    public KillAura() { super("KillAura", Category.COMBAT, GLFW.GLFW_KEY_R); }

    @Override
    public void onTick() {
        if (mc.player == null || mc.world == null || mc.currentScreen != null) return;
        if (mc.player.getAttackCooldownProgress(0.0f) < 1.0f) return;
        Box box = mc.player.getBoundingBox().expand(4.5, 3.0, 4.5);
        List<LivingEntity> targets = mc.world.getEntitiesByClass(LivingEntity.class, box, e -> {
            if (e == mc.player || e.isDead()) return false;
            if (!(e instanceof PlayerEntity p)) return false;
            if (p.isSpectator() || p.isCreative()) return false;
            if (TeamUtils.isAlly(mc, p)) return false;
            return mc.player.distanceTo(p) <= 4.5;
        });
        if (targets.isEmpty()) return;
        targets.sort((a, b) -> {
            int byDistance = Float.compare(mc.player.distanceTo(a), mc.player.distanceTo(b));
            if (byDistance != 0) return byDistance;
            return Float.compare(a.getHealth(), b.getHealth());
        });
        LivingEntity target = targets.get(0);
        mc.interactionManager.attackEntity(mc.player, target);
        mc.player.swingHand(Hand.MAIN_HAND);
    }
}
