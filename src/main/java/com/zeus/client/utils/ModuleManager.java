package com.zeus.client.utils;

import com.zeus.client.modules.combat.*;
import com.zeus.client.modules.movement.*;
import com.zeus.client.modules.player.*;
import com.zeus.client.modules.render.*;
import com.zeus.client.modules.world.*;

import java.util.ArrayList;
import java.util.List;

public class ModuleManager {
    public static final List<Module> modules = new ArrayList<>();

    public static void init() {
        add(new KillAura()); add(new AutoClicker()); add(new Criticals()); add(new Velocity());
        add(new TriggerBot()); add(new AimAssist()); add(new AutoWeapon()); add(new BowAimbot());
        add(new Reach()); add(new FastBow()); add(new AutoShield()); add(new Shiftup());
        add(new Speed()); add(new Fly()); add(new Sprint()); add(new Step()); add(new Spider());
        add(new BunnyHop()); add(new SafeWalk()); add(new NoSlow()); add(new Jesus());
        add(new LongJump()); add(new HighJump()); add(new FastClim()); add(new FastSwim());
        add(new WindHop()); add(new Phase()); add(new WaterJump()); add(new Airstate());
        add(new Scaffold()); add(new NoFall()); add(new AntiVoid()); add(new AutoArmor());
        add(new ChestStealer()); add(new FastPlace()); add(new AutoRespawn()); add(new AntiAFK());
        add(new AutoTool()); add(new AutoEat()); add(new AutoPotion()); add(new DeathCoords());
        add(new AutoTotem()); add(new BedAura());
        add(new ESP()); add(new FullBright()); add(new Zoom()); add(new NoParticles());
        add(new BlockESP()); add(new HUD());
        add(new Nuker());
    }

    private static void add(Module m) { modules.add(m); }

    public static List<Module> getByCategory(Category c) {
        List<Module> list = new ArrayList<>();
        for (Module m : modules) if (m.getCategory() == c) list.add(m);
        return list;
    }

    public static Module get(String name) {
        for (Module m : modules) if (m.getName().equals(name)) return m;
        return null;
    }

    public static boolean isEnabled(String name) {
        Module m = get(name);
        return m != null && m.isEnabled();
    }
}
