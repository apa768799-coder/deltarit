package com.zeus.client.utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.DyedColorComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;

public class TeamUtils {
    public static boolean isAlly(MinecraftClient mc, LivingEntity target) {
        ClientPlayerEntity player = mc.player;
        if (player == null || target == null) return false;
        if (target == player) return true;
        if (player.isTeammate(target)) return true;
        TextColor pc = getTextColor(player.getDisplayName());
        TextColor tc = getTextColor(target.getDisplayName());
        if (pc != null && tc != null && pc.equals(tc)) return true;
        Formatting pl = getLegacyColor(player.getDisplayName());
        Formatting tl = getLegacyColor(target.getDisplayName());
        if (pl != null && tl != null && pl == tl) return true;
        return sameArmorColor(player, target);
    }

    private static TextColor getTextColor(Text text) {
        if (text == null) return null;
        try {
            return text.getStyle().getColor();
        } catch (Throwable ignored) {
            return null;
        }
    }

    private static Formatting getLegacyColor(Text text) {
        if (text == null) return null;
        String raw = text.getString();
        for (int i = 0; i < raw.length() - 1; i++) {
            if (raw.charAt(i) == '§') {
                Formatting f = Formatting.byCode(raw.charAt(i + 1));
                if (f != null && f.isColor()) return f;
            }
        }
        return null;
    }

    private static boolean sameArmorColor(LivingEntity first, LivingEntity second) {
        int a = getArmorColor(first);
        int b = getArmorColor(second);
        return a != -1 && a == b;
    }

    private static int getArmorColor(LivingEntity entity) {
        EquipmentSlot[] slots = { EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET };
        for (EquipmentSlot slot : slots) {
            int color = getDyedColor(entity.getEquippedStack(slot));
            if (color != -1) return color;
        }
        return -1;
    }

    private static int getDyedColor(ItemStack stack) {
        try {
            if (stack == null || stack.isEmpty()) return -1;
            DyedColorComponent dyed = stack.get(DataComponentTypes.DYED_COLOR);
            if (dyed == null) return -1;
            return dyed.rgb();
        } catch (Throwable ignored) {
            return -1;
        }
    }
}
