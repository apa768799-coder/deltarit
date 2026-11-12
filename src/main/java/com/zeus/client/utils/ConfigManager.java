package com.zeus.client.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Files;
import java.nio.file.Path;

public class ConfigManager {
    private static final Gson GSON = new Gson();

    private static Path dir() {
        return FabricLoader.getInstance().getConfigDir().resolve("zeus_client").resolve("configs");
    }

    public static boolean save(String name) {
        try {
            Files.createDirectories(dir());
            JsonObject root = new JsonObject();
            for (Module m : ModuleManager.modules) {
                JsonObject o = new JsonObject();
                o.addProperty("enabled", m.isEnabled());
                o.addProperty("key", m.getKey());
                JsonObject so = new JsonObject();
                for (Setting<?> s : m.getSettings()) {
                    if (s instanceof Setting.NumberSetting n) so.addProperty(s.getName(), n.get());
                    else if (s instanceof Setting.BooleanSetting b) so.addProperty(s.getName(), b.get());
                    else if (s instanceof Setting.ModeSetting mo) so.addProperty(s.getName(), mo.get());
                }
                o.add("settings", so);
                root.add(m.getName(), o);
            }
            root.addProperty("openGuiKey", BindManager.openGuiKey);
            Files.writeString(dir().resolve(name + ".json"), GSON.toJson(root));
            return true;
        } catch (Throwable ignored) {
            return false;
        }
    }

    public static boolean load(String name) {
        try {
            Path path = dir().resolve(name + ".json");
            if (!Files.exists(path)) return false;
            JsonObject root = GSON.fromJson(Files.readString(path), JsonObject.class);
            if (root == null) return false;
            for (Module m : ModuleManager.modules) {
                if (!root.has(m.getName())) continue;
                JsonObject o = root.getAsJsonObject(m.getName());
                m.setKey(o.get("key").getAsInt());
                boolean enabled = o.get("enabled").getAsBoolean();
                if (enabled != m.isEnabled()) m.toggle();
                if (o.has("settings")) {
                    JsonObject so = o.getAsJsonObject("settings");
                    for (Setting<?> s : m.getSettings()) {
                        if (!so.has(s.getName())) continue;
                        if (s instanceof Setting.NumberSetting n) n.setDouble(so.get(s.getName()).getAsDouble());
                        else if (s instanceof Setting.BooleanSetting b) b.set(so.get(s.getName()).getAsBoolean());
                        else if (s instanceof Setting.ModeSetting mo) mo.set(so.get(s.getName()).getAsString());
                    }
                }
            }
            if (root.has("openGuiKey")) BindManager.openGuiKey = root.get("openGuiKey").getAsInt();
            return true;
        } catch (Throwable ignored) {
            return false;
        }
    }
}
