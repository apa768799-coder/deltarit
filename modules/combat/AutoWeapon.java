package com.zeus.client.modules.combat;

import com.zeus.client.utils.Category;
import com.zeus.client.utils.Module;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.lwjgl.glfw.GLFW;

public class AutoWeapon extends Module {
    public AutoWeapon() { super("AutoWeapon", Category.COMBAT, GLFW.GLFW_KEY_UNKNOWN); }

    @Override
    public void onTick() {
        if (mc.player == null) return;
        if (!mc.player.handSwinging) return;
        int best = mc.player.getInventory().selectedSlot;
        int bestRank = rank(mc.player.getInventory().getStack(best));
        for (int i = 0; i < 9; i++) {
            int r = rank(mc.player.getInventory().getStack(i));
            if (r > bestRank) { bestRank = r; best = i; }
        }
        mc.player.getInventory().selectedSlot = best;
    }

    private int rank(ItemStack s) {
        if (s.isEmpty()) return 0;
        Item i = s.getItem();
        if (i == net.minecraft.item.Items.NETHERITE_SWORD) return 7;
        if (i == net.minecraft.item.Items.DIAMOND_SWORD) return 6;
        if (i == net.minecraft.item.Items.IRON_SWORD) return 5;
        if (i == net.minecraft.item.Items.STONE_SWORD) return 4;
        if (i == net.minecraft.item.Items.GOLDEN_SWORD) return 3;
        if (i == net.minecraft.item.Items.WOODEN_SWORD) return 2;
        if (i == net.minecraft.item.Items.NETHERITE_AXE) return 7;
        if (i == net.minecraft.item.Items.DIAMOND_AXE) return 6;
        if (i == net.minecraft.item.Items.IRON_AXE) return 5;
        if (i == net.minecraft.item.Items.STONE_AXE) return 4;
        return 1;
    }
}
