package com.zeus.client.gui;

import com.zeus.client.utils.BindManager;
import com.zeus.client.utils.Category;
import com.zeus.client.utils.ConfigManager;
import com.zeus.client.utils.Module;
import com.zeus.client.utils.ModuleManager;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class ClickGuiScreen extends Screen {
    private static final int COL_W = 140;
    private static final int COL_GAP = 10;
    private static final int ROW_H = 22;
    private final int[] colScroll = new int[Category.values().length];
    private TextFieldWidget searchBox;
    private TextFieldWidget configName;
    private String searchText = "";

    public ClickGuiScreen() {
        super(Text.literal("Zeus ClickGUI"));
    }

    @Override
    protected void init() {
        this.searchBox = new TextFieldWidget(this.textRenderer,
                this.width / 2 - 100, this.height - 34, 200, 20, Text.literal("Search"));
        this.searchBox.setMaxLength(32);
        this.searchBox.setChangedListener(text -> searchText = text.toLowerCase());
        addDrawableChild(this.searchBox);
        this.configName = new TextFieldWidget(this.textRenderer,
                this.width - 172, this.height / 2 - 36, 154, 18, Text.literal("Config name"));
        this.configName.setMaxLength(24);
        addDrawableChild(this.configName);
    }

    private int colX(int index) {
        int n = Category.values().length;
        int total = n * COL_W + (n - 1) * COL_GAP;
        return (this.width - total) / 2 + index * (COL_W + COL_GAP);
    }

    private int topY() { return 40; }
    private int bottomY() { return this.height - 50; }
    private int visibleRows() { return (bottomY() - topY() - 28) / ROW_H; }

    private List<Module> filtered(Category category) {
        List<Module> list = new ArrayList<>();
        for (Module m : ModuleManager.modules) {
            if (m.getCategory() != category) continue;
            if (searchText.isEmpty() || m.getName().toLowerCase().contains(searchText)) list.add(m);
        }
        return list;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        context.fill(0, 0, this.width, this.height, 0xAA000000);
        Category[] cats = Category.values();
        for (int ci = 0; ci < cats.length; ci++) {
            int x = colX(ci);
            context.fill(x, topY(), x + COL_W, bottomY(), 0xDD101018);
            context.fill(x, topY(), x + COL_W, topY() + 1, 0xFF5555FF);
            context.drawCenteredTextWithShadow(this.textRenderer, cats[ci].name, x + COL_W / 2, topY() + 8, 0xFFFFFFFF);
            List<Module> mods = filtered(cats[ci]);
            int start = colScroll[ci];
            int count = Math.min(mods.size() - start, visibleRows());
            for (int i = 0; i < count; i++) {
                Module m = mods.get(start + i);
                int y = topY() + 28 + i * ROW_H;
                boolean hovered = mouseX >= x && mouseX <= x + COL_W && mouseY >= y && mouseY <= y + ROW_H;
                int bg = m.isEnabled() ? 0xFF223322 : (hovered ? 0xFF262636 : 0xFF181822);
                context.fill(x + 2, y, x + COL_W - 2, y + ROW_H - 2, bg);
                context.drawTextWithShadow(this.textRenderer, m.getName(), x + 8, y + 6,
                        m.isEnabled() ? 0xFF88FF88 : 0xFFDDDDDD);
                String keyName = BindManager.getKeyName(m.getKey());
                if (!keyName.equals("NONE")) {
                    context.drawTextWithShadow(this.textRenderer, keyName, x + COL_W - 34, y + 6, 0xFF888899);
                }
                context.drawTextWithShadow(this.textRenderer, "...", x + COL_W - 14, y + 6, 0xFF888899);
            }
        }
        renderConfigPanel(context, mouseX, mouseY);
        super.render(context, mouseX, mouseY, delta);
        if (BindManager.listeningModule != null) {
            context.drawCenteredTextWithShadow(this.textRenderer,
                    "Press a key for " + BindManager.listeningModule.getName(),
                    this.width / 2, this.height - 48, 0xFFFFFF55);
        }
    }

    private void renderConfigPanel(DrawContext context, int mouseX, int mouseY) {
        int x = this.width - 180;
        int y = this.height / 2 - 60;
        context.fill(x, y, x + 170, y + 120, 0xDD101018);
        context.fill(x, y, x + 170, y + 1, 0xFF5555FF);
        context.drawCenteredTextWithShadow(this.textRenderer, "Config Manager", x + 85, y + 8, 0xFFFFFFFF);
        boolean saveHover = mouseX >= x + 8 && mouseX <= x + 162 && mouseY >= y + 50 && mouseY <= y + 70;
        boolean loadHover = mouseX >= x + 8 && mouseX <= x + 162 && mouseY >= y + 76 && mouseY <= y + 96;
        context.fill(x + 8, y + 50, x + 162, y + 70, saveHover ? 0xFF2A2A4A : 0xFF1A1A2E);
        context.drawCenteredTextWithShadow(this.textRenderer, "Save", x + 85, y + 56, 0xFF88FF88);
        context.fill(x + 8, y + 76, x + 162, y + 96, loadHover ? 0xFF2A2A4A : 0xFF1A1A2E);
        context.drawCenteredTextWithShadow(this.textRenderer, "Load", x + 85, y + 82, 0xFF88AAFF);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int cx = this.width - 180;
        int cy = this.height / 2 - 60;
        String name = configName.getText().trim();
        if (!name.isEmpty() && mouseX >= cx + 8 && mouseX <= cx + 162 && mouseY >= cy + 50 && mouseY <= cy + 70) {
            ConfigManager.save(name);
            return true;
        }
        if (!name.isEmpty() && mouseX >= cx + 8 && mouseX <= cx + 162 && mouseY >= cy + 76 && mouseY <= cy + 96) {
            ConfigManager.load(name);
            return true;
        }
        Category[] cats = Category.values();
        for (int ci = 0; ci < cats.length; ci++) {
            int x = colX(ci);
            if (mouseX < x || mouseX > x + COL_W) continue;
            List<Module> mods = filtered(cats[ci]);
            int row = (int) ((mouseY - topY() - 28) / ROW_H);
            if (row < 0 || row >= visibleRows()) continue;
            int index = colScroll[ci] + row;
            if (index < 0 || index >= mods.size()) continue;
            int rowY = topY() + 28 + row * ROW_H;
            if (mouseY < rowY || mouseY > rowY + ROW_H - 2) continue;
            Module m = mods.get(index);
            if (mouseX > x + COL_W - 40) BindManager.listeningModule = m;
            else m.toggle();
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        Category[] cats = Category.values();
        for (int ci = 0; ci < cats.length; ci++) {
            int x = colX(ci);
            if (mouseX < x || mouseX > x + COL_W) continue;
            int max = Math.max(0, filtered(cats[ci]).size() - visibleRows());
            colScroll[ci] = (int) Math.max(0, Math.min(max, colScroll[ci] - verticalAmount * 2));
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (BindManager.listeningModule != null) {
            if (keyCode == GLFW.GLFW_KEY_ESCAPE || keyCode == GLFW.GLFW_KEY_BACKSPACE) {
                BindManager.listeningModule.setKey(GLFW.GLFW_KEY_UNKNOWN);
            } else {
                BindManager.listeningModule.setKey(keyCode);
            }
            BindManager.listeningModule = null;
            return true;
        }
        if (keyCode == GLFW.GLFW_KEY_ESCAPE && !searchBox.isFocused() && !configName.isFocused()) {
            close();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
}
