package com.zeus.client.utils;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class Render3DUtils {
    private static boolean ready(WorldRenderContext ctx) {
        return ctx != null && ctx.consumers() != null && ctx.matrixStack() != null && ctx.camera() != null;
    }

    public static void drawBox(WorldRenderContext ctx, Box box, int color) {
        if (!ready(ctx)) return;
        MatrixStack matrices = ctx.matrixStack();
        Vec3d cam = ctx.camera().getPos();
        matrices.push();
        matrices.translate(-cam.x, -cam.y, -cam.z);
        Matrix4f pos = matrices.peek().getPositionMatrix();
        Matrix3f norm = matrices.peek().getNormalMatrix();
        VertexConsumer vc = ctx.consumers().getBuffer(RenderLayer.getLines());
        int r = (color >> 16) & 0xFF, g = (color >> 8) & 0xFF, b = color & 0xFF;
        double x1 = box.minX, y1 = box.minY, z1 = box.minZ;
        double x2 = box.maxX, y2 = box.maxY, z2 = box.maxZ;
        line(vc, pos, norm, x1, y1, z1, x2, y1, z1, r, g, b);
        line(vc, pos, norm, x2, y1, z1, x2, y1, z2, r, g, b);
        line(vc, pos, norm, x2, y1, z2, x1, y1, z2, r, g, b);
        line(vc, pos, norm, x1, y1, z2, x1, y1, z1, r, g, b);
        line(vc, pos, norm, x1, y2, z1, x2, y2, z1, r, g, b);
        line(vc, pos, norm, x2, y2, z1, x2, y2, z2, r, g, b);
        line(vc, pos, norm, x2, y2, z2, x1, y2, z2, r, g, b);
        line(vc, pos, norm, x1, y2, z2, x1, y2, z1, r, g, b);
        line(vc, pos, norm, x1, y1, z1, x1, y2, z1, r, g, b);
        line(vc, pos, norm, x2, y1, z1, x2, y2, z1, r, g, b);
        line(vc, pos, norm, x2, y1, z2, x2, y2, z2, r, g, b);
        line(vc, pos, norm, x1, y1, z2, x1, y2, z2, r, g, b);
        matrices.pop();
        flushLines(ctx);
    }

    public static void drawTracer(WorldRenderContext ctx, Vec3d target, int color) {
        if (!ready(ctx)) return;
        MatrixStack matrices = ctx.matrixStack();
        Vec3d cam = ctx.camera().getPos();
        matrices.push();
        matrices.translate(-cam.x, -cam.y, -cam.z);
        Matrix4f pos = matrices.peek().getPositionMatrix();
        Matrix3f norm = matrices.peek().getNormalMatrix();
        VertexConsumer vc = ctx.consumers().getBuffer(RenderLayer.getLines());
        int r = (color >> 16) & 0xFF, g = (color >> 8) & 0xFF, b = color & 0xFF;
        line(vc, pos, norm, cam.x, cam.y, cam.z, target.x, target.y, target.z, r, g, b);
        matrices.pop();
        flushLines(ctx);
    }

    public static void drawCircle(WorldRenderContext ctx, Vec3d center, double radius, int color, int segments) {
        if (!ready(ctx)) return;
        if (segments < 8) segments = 8;
        MatrixStack matrices = ctx.matrixStack();
        Vec3d cam = ctx.camera().getPos();
        matrices.push();
        matrices.translate(-cam.x, -cam.y, -cam.z);
        Matrix4f pos = matrices.peek().getPositionMatrix();
        Matrix3f norm = matrices.peek().getNormalMatrix();
        VertexConsumer vc = ctx.consumers().getBuffer(RenderLayer.getLines());
        int r = (color >> 16) & 0xFF, g = (color >> 8) & 0xFF, b = color & 0xFF;
        double step = Math.PI * 2.0 / segments;
        for (int i = 0; i < segments; i++) {
            double a1 = i * step;
            double a2 = (i + 1) * step;
            line(vc, pos, norm,
                    center.x + Math.cos(a1) * radius, center.y, center.z + Math.sin(a1) * radius,
                    center.x + Math.cos(a2) * radius, center.y, center.z + Math.sin(a2) * radius,
                    r, g, b);
        }
        matrices.pop();
        flushLines(ctx);
    }

    public static void drawHalo(WorldRenderContext ctx, Entity entity, double radius, int color, int segments) {
        if (!ready(ctx)) return;
        if (segments < 8) segments = 8;
        MatrixStack matrices = ctx.matrixStack();
        Vec3d cam = ctx.camera().getPos();
        matrices.push();
        matrices.translate(-cam.x, -cam.y, -cam.z);
        matrices.translate(entity.getX(), entity.getY() + entity.getHeight() + 0.35, entity.getZ());
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-ctx.camera().getYaw()));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(ctx.camera().getPitch()));
        Matrix4f pos = matrices.peek().getPositionMatrix();
        Matrix3f norm = matrices.peek().getNormalMatrix();
        VertexConsumer vc = ctx.consumers().getBuffer(RenderLayer.getLines());
        int r = (color >> 16) & 0xFF, g = (color >> 8) & 0xFF, b = color & 0xFF;
        double step = Math.PI * 2.0 / segments;
        for (int i = 0; i < segments; i++) {
            double a1 = i * step;
            double a2 = (i + 1) * step;
            line(vc, pos, norm,
                    Math.cos(a1) * radius, Math.sin(a1) * radius, 0.0,
                    Math.cos(a2) * radius, Math.sin(a2) * radius, 0.0,
                    r, g, b);
        }
        matrices.pop();
        flushLines(ctx);
    }

    public static void drawNametag(WorldRenderContext ctx, Entity entity, String text, int color) {
        if (!ready(ctx)) return;
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.textRenderer == null) return;
        MatrixStack matrices = ctx.matrixStack();
        Vec3d cam = ctx.camera().getPos();
        matrices.push();
        matrices.translate(-cam.x, -cam.y, -cam.z);
        matrices.translate(entity.getX(), entity.getY() + entity.getHeight() + 0.4, entity.getZ());
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-ctx.camera().getYaw()));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(ctx.camera().getPitch()));
        matrices.scale(-0.025f, -0.025f, 0.025f);
        Matrix4f pos = matrices.peek().getPositionMatrix();
        float half = mc.textRenderer.getWidth(text) / 2f;
        mc.textRenderer.draw(Text.literal(text), -half, 0, color, true, pos, ctx.consumers(),
                TextRenderer.TextLayerType.SEE_THROUGH, 0, 0xF000F0);
        matrices.pop();
        if (ctx.consumers() instanceof VertexConsumerProvider.Immediate immediate) {
            immediate.draw();
        }
    }

    private static void line(VertexConsumer vc, Matrix4f pos, Matrix3f norm,
                             double x1, double y1, double z1,
                             double x2, double y2, double z2,
                             int r, int g, int b) {
        float nx = (float) (x2 - x1);
        float ny = (float) (y2 - y1);
        float nz = (float) (z2 - z1);
        float len = (float) Math.sqrt(nx * nx + ny * ny + nz * nz);
        if (len > 0) { nx /= len; ny /= len; nz /= len; }
        vc.vertex(pos, (float) x1, (float) y1, (float) z1).color(r, g, b, 255).normal(norm, nx, ny, nz).next();
        vc.vertex(pos, (float) x2, (float) y2, (float) z2).color(r, g, b, 255).normal(norm, nx, ny, nz).next();
    }

    private static void flushLines(WorldRenderContext ctx) {
        if (ctx.consumers() instanceof VertexConsumerProvider.Immediate immediate) {
            immediate.draw(RenderLayer.getLines());
        }
    }
}
