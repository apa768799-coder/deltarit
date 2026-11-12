package com.zeus.client.modules.player;

import com.zeus.client.utils.Category;
import com.zeus.client.utils.Module;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import org.lwjgl.glfw.GLFW;

public class AutoPotion extends Module {
    public AutoPotion() { super("AutoPotion", Category.PLAYER, GLFW.GLFW_KEY_UNKNOWN); }

    @Override
    public void onTick() {
        if (mc.player == null) return;
        if (mc.player.getHealth() < 12.0f) {
            int slot = findPotion();
            if (slot != -1) {
                mc.player.getInventory().selectedSlot = slot;
                mc.options.useKey.setPressed(true);
                return;
            }
        }
        if (mc.player.isUsingItem()) return;
        mc.options.useKey.setPressed(false);
    }

    @Override
    public void onDisable() {
        if (mc.player != null) mc.options.useKey.setPressed(false);
    }

    private int findPotion() {
        for (int i = 0; i < 9; i++) {
            PotionContentsComponent contents =
                    mc.player.getInventory().getStack(i).get(DataComponentTypes.POTION_CONTENTS);
            if (contents == null) continue;
            for (StatusEffectInstance effect : contents.effects()) {
                if (effect.getEffectType() == StatusEffects.HEALING
                        || effect.getEffectType() == StatusEffects.REGENERATION
                        || effect.getEffectType() == StatusEffects.STRENGTH) {
                    return i;
                }
            }
        }
        return -1;
    }
}
