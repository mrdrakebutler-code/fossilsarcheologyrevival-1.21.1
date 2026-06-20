package com.github.teamfossilsarcheology.fossil.util.neoforge;

import com.github.teamfossilsarcheology.fossil.FossilMod;
import net.neoforged.fml.loading.FMLLoader;

public class VersionImpl {
    public static String getVersion() {
        return FMLLoader.getLoadingModList().getModFileById(FossilMod.MOD_ID).versionString();
    }
}
