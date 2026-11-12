package com.zeus.client.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class Friends {
    private static final Gson GSON = new Gson();
    private static final Set<String> NAMES = new HashSet<>();
    private static final Path PATH = FabricLoader.getInstance().getConfigDir()
            .resolve("zeus_client").resolve("friends.json");

    private static String norm(String name) {
        return name == null ? "" : name.trim().toLowerCase(Locale.ROOT);
    }

    public static boolean isFriend(String name) {
        String n = norm(name);
        return !n.isEmpty() && NAMES.contains(n);
    }

    public static boolean add(String name) {
        String n = norm(name);
        if (n.isEmpty()) return false;
        boolean changed = NAMES.add(n);
        if (changed) save();
        return changed;
    }

    public static boolean remove(String name) {
        String n = norm(name);
        boolean changed = NAMES.remove(n);
        if (changed) save();
        return changed;
    }

    public static List<String> sorted() {
        List<String> list = new ArrayList<>(NAMES);
        Collections.sort(list);
        return list;
    }

    public static int size() { return NAMES.size(); }

    public static void load() {
        try {
            if (!Files.exists(PATH)) return;
            Set<String> loaded = GSON.fromJson(Files.readString(PATH),
                    new TypeToken<Set<String>>() {}.getType());
            if (loaded == null) return;
            NAMES.clear();
            for (String s : loaded) {
                String n = norm(s);
                if (!n.isEmpty()) NAMES.add(n);
            }
        } catch (Throwable ignored) {
        }
    }

    public static void save() {
        try {
            Files.createDirectories(PATH.getParent());
            Files.writeString(PATH, GSON.toJson(NAMES));
        } catch (Throwable ignored) {
        }
    }
}
