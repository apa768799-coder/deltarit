package com.zeus.client.modules.player;

import com.zeus.client.utils.Category;
import com.zeus.client.utils.Module;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.slot.SlotActionType;
import org.lwjgl.glfw.GLFW;

public class ChestStealer extends Module {
    public ChestStealer() { super("ChestStealer", Category.PLAYER, GLFW.GLFW_KEY_UNKNOWN); }

    @Override
    public void onTick() {
        if (mc.player == null || mc.interactionManager == null) return;
        if (!(mc.player.currentScreenHandler instanceof GenericContainerScreenHandler handler)) return;
        int containerSlots = handler.slots.size() - 36;
        boolean empty = true;
        for (int s = 0; s < containerSlots; s++) {
            if (!handler.getSlot(s).getStack().isEmpty()) {
                empty = false;
                mc.interactionManager.clickSlot(handler.syncId, s, 0, SlotActionType.QUICK_MOVE, mc.player);
            }
        }
        if (empty) mc.player.closeHandledScreen();
    }
}
