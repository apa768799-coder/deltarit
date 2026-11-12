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
            }
            if (root.has("openGuiKey")) BindManager.openGuiKey = root.get("openGuiKey").getAsInt();
            return true;
        } catch (Throwable ignored) {
            return false;
        }
    }
}
