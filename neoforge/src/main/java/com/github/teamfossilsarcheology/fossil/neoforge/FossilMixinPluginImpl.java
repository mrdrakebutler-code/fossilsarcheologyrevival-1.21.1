package com.github.teamfossilsarcheology.fossil.neoforge;

import net.neoforged.fml.loading.FMLLoader;

/**
 * @see com.github.teamfossilsarcheology.fossil.FossilMixinPlugin
 */
public class FossilMixinPluginImpl {
    public static boolean isModLoaded(String mod) {
        return FMLLoader.getLoadingModList().getModFileById(mod) != null;
    }
}
