package com.zeus.client.utils;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.MinecraftClient;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class Module {
    protected final MinecraftClient mc = MinecraftClient.getInstance();
    public static Consumer<String> notifier = null;

    private final String name;
    private final Category category;
    private int key;
    private boolean enabled;
    private boolean keyPressedLastTick = false;
    protected final List<Setting<?>> settings = new ArrayList<>();

    public Module(String name, Category category, int key) {
        this.name = name;
        this.category = category;
        this.key = key;
    }

    public String getName() { return name; }
    public Category getCategory() { return category; }
    public int getKey() { return key; }
    public void setKey(int key) { this.key = key; }
    public boolean isEnabled() { return enabled; }
    public boolean isKeyPressedLastTick() { return keyPressedLastTick; }
    public void setKeyPressedLastTick(boolean val) { this.keyPressedLastTick = val; }
    public boolean runsInScreens() { return false; }
    public List<Setting<?>> getSettings() { return settings; }

    protected <S extends Setting<?>> S reg(S setting) {
        settings.add(setting);
        return setting;
    }

    public void toggle() {
        this.enabled = !this.enabled;
        if (this.enabled) onEnable(); else onDisable();
        if (notifier != null) notifier.accept(name + (this.enabled ? " ON" : " OFF"));
    }

    public void onEnable() {}
    public void onDisable() {}
    public void onTick() {}
    public void onRender3D(WorldRenderContext context) {}
}
