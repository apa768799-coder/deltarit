package com.zeus.client.modules.player;

import com.zeus.client.utils.Category;
import com.zeus.client.utils.InventoryUtils;
import com.zeus.client.utils.Module;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

public class AntiVoid extends Module {
    public AntiVoid() { super("AntiVoid", Category.PLAYER, GLFW.GLFW_KEY_UNKNOWN); }

    @Override
    public void onTick() {
        if (mc.player == null || mc.world == null || mc.interactionManager == null) return;
        if (mc.player.getVelocity().y >= -0.05) return;
        if (mc.player.fallDistance < 3.0f) return;
        BlockPos below = mc.player.getBlockPos().down();
        if (!mc.world.getBlockState(below).isAir()) return;
        int slot = InventoryUtils.findBlockSlot();
        if (slot == -1) return;
        int old = mc.player.getInventory().selectedSlot;
        mc.player.getInventory().selectedSlot = slot;
        mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND,
                new BlockHitResult(Vec3d.ofCenter(below), Direction.UP, below, false));
        mc.player.getInventory().selectedSlot = old;
    }
}
