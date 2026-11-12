package com.zeus.client.modules.player;

import com.zeus.client.utils.Category;
import com.zeus.client.utils.InventoryUtils;
import com.zeus.client.utils.Module;
import net.minecraft.block.BlockState;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

public class Scaffold extends Module {
    public Scaffold() { super("Scaffold", Category.PLAYER, GLFW.GLFW_KEY_G); }

    @Override
    public void onTick() {
        if (mc.player == null || mc.world == null || mc.interactionManager == null || mc.currentScreen != null) return;
        BlockPos below = mc.player.getBlockPos().down();
        BlockState state = mc.world.getBlockState(below);
        if (state.isAir() && !mc.player.isSneaking()) {
            int slot = InventoryUtils.findBlockSlot();
            if (slot != -1) {
                int old = mc.player.getInventory().selectedSlot;
                mc.player.getInventory().selectedSlot = slot;
                Vec3d hitVec = Vec3d.ofCenter(below);
                mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND,
                        new BlockHitResult(hitVec, Direction.UP, below, false));
                mc.player.getInventory().selectedSlot = old;
            }
        }
    }
}
