package com.zeus.client.modules.player;

import com.zeus.client.utils.Category;
import com.zeus.client.utils.Module;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.BlockHitResult;
import org.lwjgl.glfw.GLFW;

public class AutoTool extends Module {
    public AutoTool() { super("AutoTool", Category.PLAYER, GLFW.GLFW_KEY_UNKNOWN); }

    @Override
    public void onTick() {
        if (mc.player == null || mc.world == null) return;
        if (!(mc.crosshairTarget instanceof BlockHitResult hit)) return;
        BlockState state = mc.world.getBlockState(hit.getBlockPos());
        if (state.isAir()) return;
        int best = -1;
        float bestSpeed = 1.0f;
        for (int i = 0; i < 9; i++) {
            ItemStack s = mc.player.getInventory().getStack(i);
            float sp = s.getMiningSpeedMultiplier(state);
            if (sp > bestSpeed) { bestSpeed = sp; best = i; }
        }
        if (best != -1) mc.player.getInventory().selectedSlot = best;
    }
}
