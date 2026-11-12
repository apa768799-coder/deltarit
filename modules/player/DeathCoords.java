package com.zeus.client.modules.player;

import com.zeus.client.utils.Category;
import com.zeus.client.utils.Module;
import net.minecraft.client.gui.screen.DeathScreen;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.glfw.GLFW;

public class DeathCoords extends Module {
    private BlockPos lastPos = null;
    private boolean sent = false;

    public DeathCoords() { super("DeathCoords", Category.PLAYER, GLFW.GLFW_KEY_UNKNOWN); }

    @Override
    public boolean runsInScreens() { return true; }

    @Override
    public void onTick() {
        if (mc.currentScreen instanceof DeathScreen) {
            if (!sent && lastPos != null) {
                mc.inGameHud.getChatHud().addMessage(Text.literal(
                        "Zeus | Death at " + lastPos.getX() + " " + lastPos.getY() + " " + lastPos.getZ()));
                sent = true;
            }
        } else {
            sent = false;
            if (mc.player != null) lastPos = mc.player.getBlockPos();
        }
    }
}
