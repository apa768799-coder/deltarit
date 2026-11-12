package com.zeus.client.modules.combat;

import com.zeus.client.utils.Category;
import com.zeus.client.utils.Module;
import net.minecraft.item.BowItem;
import org.lwjgl.glfw.GLFW;

public class FastBow extends Module {
    private boolean releaseTick = false;

    public FastBow() { super("FastBow", Category.COMBAT, GLFW.GLFW_KEY_UNKNOWN); }

    @Override
    public void onTick() {
        if (mc.player == null) return;
        if (releaseTick) {
            mc.options.useKey.setPressed(true);
            releaseTick = false;
            return;
        }
        if (mc.player.isUsingItem()
                && mc.player.getActiveItem().getItem() instanceof BowItem
                && mc.player.getItemUseTime() >= 20) {
            mc.options.useKey.setPressed(false);
            releaseTick = true;
        }
    }

    @Override
    public void onDisable() {
        if (mc.player != null) mc.options.useKey.setPressed(false);
    }
}
