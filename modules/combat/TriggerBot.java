package com.zeus.client.modules.combat;

import com.zeus.client.utils.Category;
import com.zeus.client.utils.Module;
import com.zeus.client.utils.TeamUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import org.lwjgl.glfw.GLFW;

public class TriggerBot extends Module {
    public TriggerBot() { super("TriggerBot", Category.COMBAT, GLFW.GLFW_KEY_UNKNOWN); }

    @Override
    public void onTick() {
        if (mc.player == null || mc.world == null || mc.currentScreen != null) return;
        if (mc.player.getAttackCooldownProgress(0.0f) < 1.0f) return;
        if (!(mc.crosshairTarget instanceof EntityHitResult hit)) return;
        Entity target = hit.getEntity();
        if (!(target instanceof PlayerEntity p)) return;
        if (p.isDead() || p.isSpectator() || p.isCreative()) return;
        if (TeamUtils.isAlly(mc, p)) return;
        mc.interactionManager.attackEntity(mc.player, target);
        mc.player.swingHand(Hand.MAIN_HAND);
    }
}
