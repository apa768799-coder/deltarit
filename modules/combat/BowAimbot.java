package com.zeus.client.modules.combat;

import com.zeus.client.utils.Category;
import com.zeus.client.utils.Module;
import com.zeus.client.utils.TeamUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BowItem;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.glfw.GLFW;

import java.util.List;

public class BowAimbot extends Module {
    public BowAimbot() { super("BowAimbot", Category.COMBAT, GLFW.GLFW_KEY_UNKNOWN); }

    @Override
    public void onTick() {
        if (mc.player == null || mc.world == null || mc.currentScreen != null) return;
        if (!mc.player.isUsingItem()) return;
        if (!(mc.player.getActiveItem().getItem() instanceof BowItem)) return;
        int useTicks = mc.player.getItemUseTime();
        float p = MathHelper.clamp(useTicks / 20.0f, 0.0f, 1.0f);
        p = (p * p + p * 2.0f) / 3.0f;
        if (p > 1.0f) p = 1.0f;
        float speed = p * 3.0f;
        if (speed < 0.1f) return;
        List<LivingEntity> targets = mc.world.getEntitiesByClass(LivingEntity.class,
                mc.player.getBoundingBox().expand(30.0),
                e -> e instanceof PlayerEntity && e != mc.player && !e.isDead() && !TeamUtils.isAlly(mc, e));
        if (targets.isEmpty()) return;
        targets.sort((a, b) -> Float.compare(mc.player.distanceTo(a), mc.player.distanceTo(b)));
        LivingEntity target = targets.get(0);
        double dx = target.getX() - mc.player.getX();
        double dy = target.getY() + target.getStandingEyeHeight() - (mc.player.getY() + mc.player.getStandingEyeHeight());
        double dz = target.getZ() - mc.player.getZ();
        double dist = Math.sqrt(dx * dx + dz * dz);
        double t = dist / speed;
        double dyAdj = dy - 0.05 * t * t;
        mc.player.setYaw((float) (Math.atan2(dz, dx) * 180.0 / Math.PI) - 90.0f);
        mc.player.setPitch((float) (-Math.atan2(dyAdj, dist) * 180.0 / Math.PI));
    }
}
