package com.zeus.client.utils;

import java.util.List;

public abstract class Setting<T> {
    private final String name;
    private T value;
    private final T defaultValue;

    protected Setting(String name, T value) {
        this.name = name;
        this.value = value;
        this.defaultValue = value;
    }

    public String getName() { return name; }
    public T get() { return value; }
    public void set(T value) { this.value = value; }
    public T getDefault() { return defaultValue; }

    public static class NumberSetting extends Setting<Double> {
        private final double min;
        private final double max;
        private final double step;

        public NumberSetting(String name, double value, double min, double max, double step) {
            super(name, value);
            this.min = min;
            this.max = max;
            this.step = step;
        }

        public double getMin() { return min; }
        public double getMax() { return max; }

        public void setDouble(double v) {
            v = Math.max(min, Math.min(max, v));
            v = Math.round(v / step) * step;
            set(v);
        }
    }

    public static class BooleanSetting extends Setting<Boolean> {
        public BooleanSetting(String name, boolean value) { super(name, value); }
        public void toggle() { set(!get()); }
    }

    public static class ModeSetting extends Setting<String> {
        private final List<String> modes;

        public ModeSetting(String name, List<String> modes) {
            super(name, modes.get(0));
            this.modes = modes;
        }

        public List<String> getModes() { return modes; }

        public void cycle() {
            int i = modes.indexOf(get());
            set(modes.get((i + 1) % modes.size()));
        }
    }
}
