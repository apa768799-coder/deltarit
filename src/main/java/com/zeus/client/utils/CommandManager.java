package com.zeus.client.utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class CommandManager {
    public static boolean handle(String message) {
        if (message == null || !message.startsWith(".")) return false;
        MinecraftClient mc = MinecraftClient.getInstance();
        String[] args = message.substring(1).trim().split("\\s+");
        if (args.length == 0 || args[0].isEmpty()) return true;

        switch (args[0].toLowerCase()) {
            case "help" -> feedback(mc, ".t <mod> | .bind <mod> <key> | .friend add/remove/list | .cfg save/load <name>");
            case "t", "toggle" -> {
                if (args.length < 2) { feedback(mc, "Usage: .t <module>"); break; }
                Module m = ModuleManager.get(args[1]);
                if (m != null) {
                    m.toggle();
                    feedback(mc, m.getName() + " " + (m.isEnabled() ? "ON" : "OFF"));
                } else {
                    feedback(mc, "Module not found: " + args[1]);
                }
            }
            case "bind" -> {
                if (args.length < 3) { feedback(mc, "Usage: .bind <module> <key>"); break; }
                Module m = ModuleManager.get(args[1]);
                int key = parseKey(args[2]);
                if (m == null) feedback(mc, "Module not found: " + args[1]);
                else if (key == -1) feedback(mc, "Unknown key: " + args[2]);
                else {
                    m.setKey(key);
                    feedback(mc, m.getName() + " bound to " + args[2].toUpperCase());
                }
            }
            case "friend" -> {
                if (args.length < 2) { feedback(mc, "Usage: .friend add/remove <name> | .friend list"); break; }
                if (args[1].equalsIgnoreCase("list")) {
                    if (Friends.size() == 0) feedback(mc, "Friend list is empty");
                    else feedback(mc, "Friends: " + String.join(", ", Friends.sorted()));
                } else if (args.length < 3) {
                    feedback(mc, "Usage: .friend add/remove <name>");
                } else if (args[1].equalsIgnoreCase("add")) {
                    feedback(mc, Friends.add(args[2]) ? "Friend added: " + args[2] : "Already added: " + args[2]);
                } else if (args[1].equalsIgnoreCase("remove")) {
                    feedback(mc, Friends.remove(args[2]) ? "Friend removed: " + args[2] : "Not in list: " + args[2]);
                }
            }
            case "cfg" -> {
                if (args.length < 3) { feedback(mc, "Usage: .cfg save/load <name>"); break; }
                boolean ok = args[1].equalsIgnoreCase("save")
                        ? ConfigManager.save(args[2])
                        : ConfigManager.load(args[2]);
                feedback(mc, (ok ? "Config ok: " : "Config failed: ") + args[2]);
            }
            default -> feedback(mc, "Unknown command. Type .help");
        }
        return true;
    }

    private static int parseKey(String name) {
        for (int k = 32; k < 350; k++) {
            String n = GLFW.glfwGetKeyName(k, GLFW.glfwGetKeyScancode(k));
            if (n != null && n.equalsIgnoreCase(name)) return k;
        }
        return -1;
    }

    private static void feedback(MinecraftClient mc, String text) {
        if (mc.inGameHud != null) {
            mc.inGameHud.getChatHud().addMessage(Text.literal("Zeus | " + text));
        }
    }
}
