package com.zeus.client.modules.player;

import com.zeus.client.utils.Category;
import com.zeus.client.utils.Module;
import net.minecraft.component.DataComponentTypes;
import org.lwjgl.glfw.GLFW;

public class AutoEat extends Module {
    public AutoEat() { super("AutoEat", Category.PLAYER, GLFW.GLFW_KEY_UNKNOWN); }

    @Override
    public void onTick() {
        if (mc.player == null) return;
        if (mc.player.getHungerManager().getFoodLevel() < 14) {
            int slot = findFood();
            if (slot != -1) {
                mc.player.getInventory().selectedSlot = slot;
                mc.options.useKey.setPressed(true);
                return;
            }
        }
        mc.options.useKey.setPressed(false);
    }

    @Override
    public void onDisable() {
        if (mc.player != null) mc.options.useKey.setPressed(false);
    }

    private int findFood() {
        for (int i = 0; i < 9; i++) {
            if (mc.player.getInventory().getStack(i).get(DataComponentTypes.FOOD) != null) return i;
        }
        return -1;
    }
}
