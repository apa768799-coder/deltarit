package com.zeus.client.modules.render;

import com.zeus.client.utils.Category;
import com.zeus.client.utils.Module;
import net.minecraft.block.BedBlock;
import net.minecraft.block.ChestBlock;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class BlockESP extends Module {
    private final List<BlockPos> beds = new ArrayList<>();
    private final List<BlockPos> chests = new ArrayList<>();
    private int scanCycle = 0;

    public BlockESP() { super("BlockESP", Category.RENDER, GLFW.GLFW_KEY_UNKNOWN); }

    @Override
    public void onTick() {
        if (mc.player == null || mc.world == null) return;
        if (scanCycle++ % 20 != 0) return;
        beds.clear();
        chests.clear();
        BlockPos origin = mc.player.getBlockPos();
        for (int dx = -16; dx <= 16; dx++) {
            for (int dy = -8; dy <= 8; dy++) {
                for (int dz = -16; dz <= 16; dz++) {
                    BlockPos pos = origin.add(dx, dy, dz);
                    var block = mc.world.getBlockState(pos).getBlock();
                    if (block instanceof BedBlock) beds.add(pos);
                    else if (block instanceof ChestBlock) chests.add(pos);
                }
            }
        }
    }

    public static void render(DrawContext context, BlockESP self) {
        if (self.mc.player == null || self.mc.textRenderer == null || self.mc.getWindow() == null) return;
        int cx = self.mc.getWindow().getScaledWidth() / 2;
        int cy = self.mc.getWindow().getScaledHeight() / 2;
        self.drawMarkers(context, self.beds, cx, cy, 0xFFFF5555, "B");
        self.drawMarkers(context, self.chests, cx, cy, 0xFFFFAA55, "C");
    }

    private void drawMarkers(DrawContext context, List<BlockPos> list, int cx, int cy, int color, String label) {
        for (BlockPos pos : list) {
            double dx = pos.getX() + 0.5 - mc.player.getX();
            double dz = pos.getZ() + 0.5 - mc.player.getZ();
            double dist = Math.sqrt(dx * dx + dz * dz);
            if (dist > 32) continue;
            double targetYaw = Math.atan2(dz, dx) * 180.0 / Math.PI - 90.0;
            double rel = Math.toRadians(targetYaw - mc.player.getYaw());
            int x = cx + (int) (Math.sin(rel) * -80);
            int y = cy - 40;
            context.drawTextWithShadow(mc.textRenderer, label + " " + (int) dist + "m", x, y, color);
        }
    }
}
