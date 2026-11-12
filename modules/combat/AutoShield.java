package com.zeus.client.modules.combat;

import com.zeus.client.utils.Category;
import com.zeus.client.utils.Module;
import com.zeus.client.utils.TeamUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import org.lwjgl.glfw.GLFW;

public class AutoShield extends Module {
    public AutoShield() { super("AutoShield", Category.COMBAT, GLFW.GLFW_KEY_UNKNOWN); }

    @Override
    public void onTick() {
        if (mc.player == null || mc.world == null) return;
        boolean hasShield = mc.player.getOffHandStack().isOf(Items.SHIELD)
                || mc.player.getMainHandStack().isOf(Items.SHIELD);
        if (!hasShield) {
            mc.options.useKey.setPressed(false);
            return;
        }
        boolean threat = false;
        for (PlayerEntity p : mc.world.getPlayers()) {
            if (p == mc.player || p.isDead()) continue;
            if (TeamUtils.isAlly(mc, p)) continue;
            if (mc.player.distanceTo(p) <= 4.0f && p.handSwinging) { threat = true; break; }
        }
        mc.options.useKey.setPressed(threat);
    }

    @Override
    public void onDisable() {
        if (mc.player != null) mc.options.useKey.setPressed(false);
    }
}
