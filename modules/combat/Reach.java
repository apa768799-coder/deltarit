package com.zeus.client.modules.combat;

import com.zeus.client.utils.Category;
import com.zeus.client.utils.Module;
import com.zeus.client.utils.TeamUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import org.lwjgl.glfw.GLFW;

import java.util.List;

public class Reach extends Module {
    public Reach() { super("Reach", Category.COMBAT, GLFW.GLFW_KEY_UNKNOWN); }

    @Override
    public void onTick() {
        if (mc.player == null || mc.world == null || mc.currentScreen != null) return;
        if (mc.player.getAttackCooldownProgress(0.0f) < 1.0f) return;
        List<LivingEntity> targets = mc.world.getEntitiesByClass(LivingEntity.class,
                mc.player.getBoundingBox().expand(6.0, 3.0, 6.0),
                e -> e instanceof PlayerEntity p && e != mc.player && !e.isDead()
                        && !p.isSpectator() && !p.isCreative() && !TeamUtils.isAlly(mc, p));
        if (targets.isEmpty()) return;
        targets.sort((a, b) -> Float.compare(mc.player.distanceTo(a), mc.player.distanceTo(b)));
        mc.interactionManager.attackEntity(mc.player, targets.get(0));
        mc.player.swingHand(Hand.MAIN_HAND);
    }
}
