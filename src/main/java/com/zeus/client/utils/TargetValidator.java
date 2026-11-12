package com.zeus.client.utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.EntityHitResult;

public class TargetValidator {
    public static boolean protectFriends = true;

    public static boolean isFriend(Entity entity) {
        if (!(entity instanceof PlayerEntity p)) return false;
        return Friends.isFriend(p.getName().getString());
    }

    public static boolean isProtected(MinecraftClient mc, Entity entity) {
        if (mc == null || entity == null) return false;
        if (entity == mc.player) return true;
        if (protectFriends && isFriend(entity)) return true;
        return entity instanceof LivingEntity living && TeamUtils.isAlly(mc, living);
    }

    public static boolean isValidTarget(MinecraftClient mc, Entity entity) {
        if (mc == null || mc.player == null || entity == null) return false;
        if (entity == mc.player) return false;
        if (!(entity instanceof PlayerEntity p)) return false;
        if (p.isDead() || p.getHealth() <= 0.0f) return false;
        if (p.isSpectator() || p.isCreative()) return false;
        if (protectFriends && isFriend(p)) return false;
        return !TeamUtils.isAlly(mc, p);
    }

    public static boolean isValidTarget(MinecraftClient mc, Entity entity, double range) {
        if (!isValidTarget(mc, entity)) return false;
        return mc.player.distanceTo(entity) <= range;
    }

    public static boolean isValidMob(MinecraftClient mc, Entity entity, boolean hostileOnly, boolean skipTamed) {
        if (mc == null || mc.player == null || entity == null) return false;
        if (entity == mc.player) return false;
        if (!(entity instanceof LivingEntity living)) return false;
        if (living.isDead() || living.getHealth() <= 0.0f) return false;
        if (entity instanceof PlayerEntity) return false;
        if (entity instanceof ArmorStandEntity) return false;
        if (skipTamed && entity instanceof TameableEntity tame && tame.isTamed()) return false;
        if (hostileOnly && !(entity instanceof Monster)) return false;
        return true;
    }

    public static boolean crosshairProtected(MinecraftClient mc) {
        if (mc == null || !(mc.crosshairTarget instanceof EntityHitResult hit)) return false;
        return isProtected(mc, hit.getEntity());
    }
}
