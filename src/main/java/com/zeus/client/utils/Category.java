package com.zeus.client.utils;

public enum Category {
    COMBAT("Combat"),
    PVE("PvE"),
    MOVEMENT("Movement"),
    RENDER("Visuals"),
    PLAYER("Player"),
    WORLD("World");

    public final String name;
    Category(String name) { this.name = name; }
}
