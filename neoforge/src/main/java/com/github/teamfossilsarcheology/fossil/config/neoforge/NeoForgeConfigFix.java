package com.github.teamfossilsarcheology.fossil.config.neoforge;

import com.github.teamfossilsarcheology.fossil.FossilMod;
import com.github.teamfossilsarcheology.fossil.config.FossilConfig;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.*;
import java.util.function.Predicate;

/**
 * Simple way to forcefully update user configs
 */
public class NeoForgeConfigFix {
    private static final Map<Integer, Set<Update<?>>> FIXES = new HashMap<>();

    static {
    }

    private static <T> void add(int version, ModConfigSpec.ConfigValue<T> spec, T newValue, Predicate<T> predicate) {
        FIXES.computeIfAbsent(version, k -> new HashSet<>()).add(new Update<>(spec.getPath(), newValue, predicate));
    }

    private static <T> void add(int version, ModConfigSpec.ConfigValue<T> spec, T newValue) {
        add(version, spec, newValue, t -> true);
    }

    public static void fixConfig(ModConfigEvent.Loading event) {
        if (event.getConfig().getModId().equals(FossilMod.MOD_ID) && event.getConfig().getType() == ModConfig.Type.COMMON) {
            // NeoForge exposes the raw NightConfig via ILoadedConfig#config() instead of ModConfig#getConfigData().
            var loaded = event.getConfig().getLoadedConfig();
            if (loaded == null) {
                return;
            }
            var config = loaded.config();
            var version = config.get(NeoForgeConfig.VERSION.getPath());
            if (version instanceof Integer v && v < FossilConfig.VERSION_VALUE) {
                FossilMod.LOGGER.info("Config version is outdated: {} -> {}", v, FossilConfig.VERSION_VALUE);
                for (Update<?> update : FIXES.getOrDefault(FossilConfig.VERSION_VALUE, new HashSet<>())) {
                    Object object = config.get(update.path);
                    if (object != null && update.canReplace(object)) {
                        FossilMod.LOGGER.info("Updating {}: {} -> {}", update.path, object, update.newValue);
                        config.set(update.path, update.newValue);
                    }
                }
                config.set(NeoForgeConfig.VERSION.getPath(), FossilConfig.VERSION_VALUE);
            }
        }
    }

    private record Update<T>(List<String> path, T newValue, Predicate<T> predicate) {
        public boolean canReplace(Object oldValue) {
            return oldValue.getClass().isAssignableFrom(newValue.getClass()) && predicate.test((T) oldValue);
        }
    }
}
