package com.zeus.client.modules.render;

import com.zeus.client.utils.Category;
import com.zeus.client.utils.Module;
import com.zeus.client.utils.ModuleManager;
import com.zeus.client.utils.TeamUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.registry.Registries;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class HUD extends Module {
    private static final List<String> notifications = new ArrayList<>();
    private static final List<Long> notificationTimes = new ArrayList<>();
    private static long lastFrame = System.nanoTime();
    private static double fps = 0;

    public HUD() { super("HUD", Category.RENDER, GLFW.GLFW_KEY_UNKNOWN); }

    public static void notify(String message) {
        notifications.add(message);
        notificationTimes.add(System.currentTimeMillis());
        if (notifications.size() > 6) {
            notifications.remove(0);
            notificationTimes.remove(0);
        }
    }

    public static void render(DrawContext context) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.textRenderer == null || mc.getWindow() == null) return;
        int w = mc.getWindow().getScaledWidth();
        int h = mc.getWindow().getScaledHeight();
        long now = System.nanoTime();
        double instant = 1_000_000_000.0 / Math.max(1, now - lastFrame);
        fps = fps * 0.9 + instant * 0.1;
        lastFrame = now;
        int y = 4;
        y = line(context, "FPS: " + (int) fps, 4, y);
        if (mc.player != null) {
            var pos = mc.player.getBlockPos();
            y = line(context, "XYZ: " + pos.getX() + " " + pos.getY() + " " + pos.getZ(), 4, y);
            if (mc.getNetworkHandler() != null) {
                PlayerListEntry entry = mc.getNetworkHandler().getPlayerListEntry(mc.player.getGameProfile().getId());
                if (entry != null) y = line(context, "Ping: " + entry.getLatency() + "ms", 4, y);
            }
            var target = nearestEnemy(mc);
            if (target != null) {
                y = line(context, "Target: " + target.getName().getString()
                        + " | " + (int) target.getHealth() + "hp | "
                        + (int) mc.player.distanceTo(target) + "m", 4, y);
            }
            for (StatusEffectInstance effect : mc.player.getStatusEffects()) {
                String id = Registries.STATUS_EFFECT.getId(effect.getEffectType().value()).getPath();
                y = line(context, id + " " + (effect.getDuration() / 20) + "s", 4, y);
            }
        }
        renderArrayList(context, mc, w);
        renderNotifications(context, mc, w, h);
    }

    private static int line(DrawContext context, String text, int x, int y) {
        context.drawTextWithShadow(MinecraftClient.getInstance().textRenderer, text, x, y, 0xFFDDDDDD);
        return y + 10;
    }

    private static net.minecraft.entity.player.PlayerEntity nearestEnemy(MinecraftClient mc) {
        if (mc.world == null) return null;
        net.minecraft.entity.player.PlayerEntity best = null;
        for (var p : mc.world.getPlayers()) {
            if (p == mc.player || p.isDead()) continue;
            if (TeamUtils.isAlly(mc, p)) continue;
            if (best == null || mc.player.distanceTo(p) < mc.player.distanceTo(best)) best = p;
        }
        return best != null && mc.player.distanceTo(best) <= 10.0f ? best : null;
    }

    private static void renderArrayList(DrawContext context, MinecraftClient mc, int w) {
        List<Module> enabled = new ArrayList<>();
        for (Module m : ModuleManager.modules) if (m.isEnabled()) enabled.add(m);
        enabled.sort((a, b) -> Integer.compare(
                mc.textRenderer.getWidth(b.getName()), mc.textRenderer.getWidth(a.getName())));
        int y = 4;
        int i = 0;
        for (Module m : enabled) {
            int tw = mc.textRenderer.getWidth(m.getName());
            int color = 0xFF000000 | MathHelper.hsvToRgb((i * 0.08f) % 1.0f, 0.7f, 1.0f);
            context.drawTextWithShadow(mc.textRenderer, m.getName(), w - tw - 4, y, color);
            y += 10;
            i++;
        }
    }

    private static void renderNotifications(DrawContext context, MinecraftClient mc, int w, int h) {
        long now = System.currentTimeMillis();
        int y = h - 20;
        for (int i = notifications.size() - 1; i >= 0; i--) {
            long age = now - notificationTimes.get(i);
            if (age > 4000) {
                notifications.remove(i);
                notificationTimes.remove(i);
                continue;
            }
            int alpha = (int) (255 * Math.min(1.0, (4000 - age) / 1000.0));
            String text = notifications.get(i);
            int tw = mc.textRenderer.getWidth(text);
            context.drawTextWithShadow(mc.textRenderer, text, w - tw - 4, y, (alpha << 24) | 0x88FF88);
            y -= 10;
        }
    }
}
