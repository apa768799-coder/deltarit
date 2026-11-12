package com.zeus.client.modules.player;

import com.zeus.client.utils.Category;
import com.zeus.client.utils.Module;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.SlotActionType;
import org.lwjgl.glfw.GLFW;

public class AutoArmor extends Module {
    private int cycle = 0;

    public AutoArmor() { super("AutoArmor", Category.PLAYER, GLFW.GLFW_KEY_UNKNOWN); }

    @Override
    public void onTick() {
        if (mc.player == null || mc.interactionManager == null || mc.currentScreen != null) return;
        if (cycle++ % 5 != 0) return;
        for (int armorSlot = 5; armorSlot <= 8; armorSlot++) {
            int currentRank = rank(mc.player.currentScreenHandler.getSlot(armorSlot).getStack(), armorSlot);
            int best = -1;
            int bestRank = currentRank;
            for (int i = 9; i <= 44; i++) {
                int r = rank(mc.player.currentScreenHandler.getSlot(i).getStack(), armorSlot);
                if (r > bestRank) { bestRank = r; best = i; }
            }
            if (best != -1) {
                int syncId = mc.player.currentScreenHandler.syncId;
                mc.interactionManager.clickSlot(syncId, best, 0, SlotActionType.PICKUP, mc.player);
                mc.interactionManager.clickSlot(syncId, armorSlot, 0, SlotActionType.PICKUP, mc.player);
                return;
            }
        }
    }

    private int rank(ItemStack s, int armorSlot) {
        if (s.isEmpty()) return 0;
        Item i = s.getItem();
        return switch (armorSlot) {
            case 5 -> helmet(i);
            case 6 -> chest(i);
            case 7 -> legs(i);
            case 8 -> boots(i);
            default -> 0;
        };
    }

    private int helmet(Item i) {
        if (i == net.minecraft.item.Items.NETHERITE_HELMET) return 6;
        if (i == net.minecraft.item.Items.DIAMOND_HELMET) return 5;
        if (i == net.minecraft.item.Items.IRON_HELMET) return 4;
        if (i == net.minecraft.item.Items.CHAINMAIL_HELMET) return 3;
        if (i == net.minecraft.item.Items.GOLDEN_HELMET) return 2;
        if (i == net.minecraft.item.Items.LEATHER_HELMET) return 1;
        return 0;
    }

    private int chest(Item i) {
        if (i == net.minecraft.item.Items.NETHERITE_CHESTPLATE) return 6;
        if (i == net.minecraft.item.Items.DIAMOND_CHESTPLATE) return 5;
        if (i == net.minecraft.item.Items.IRON_CHESTPLATE) return 4;
        if (i == net.minecraft.item.Items.CHAINMAIL_CHESTPLATE) return 3;
        if (i == net.minecraft.item.Items.GOLDEN_CHESTPLATE) return 2;
        if (i == net.minecraft.item.Items.LEATHER_CHESTPLATE) return 1;
        return 0;
    }

    private int legs(Item i) {
        if (i == net.minecraft.item.Items.NETHERITE_LEGGINGS) return 6;
        if (i == net.minecraft.item.Items.DIAMOND_LEGGINGS) return 5;
        if (i == net.minecraft.item.Items.IRON_LEGGINGS) return 4;
        if (i == net.minecraft.item.Items.CHAINMAIL_LEGGINGS) return 3;
        if (i == net.minecraft.item.Items.GOLDEN_LEGGINGS) return 2;
        if (i == net.minecraft.item.Items.LEATHER_LEGGINGS) return 1;
        return 0;
    }

    private int boots(Item i) {
        if (i == net.minecraft.item.Items.NETHERITE_BOOTS) return 6;
        if (i == net.minecraft.item.Items.DIAMOND_BOOTS) return 5;
        if (i == net.minecraft.item.Items.IRON_BOOTS) return 4;
        if (i == net.minecraft.item.Items.CHAINMAIL_BOOTS) return 3;
        if (i == net.minecraft.item.Items.GOLDEN_BOOTS) return 2;
        if (i == net.minecraft.item.Items.LEATHER_BOOTS) return 1;
        return 0;
    }
}
