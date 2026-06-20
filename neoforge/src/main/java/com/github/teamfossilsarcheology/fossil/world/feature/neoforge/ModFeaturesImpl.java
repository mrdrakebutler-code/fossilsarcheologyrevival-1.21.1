package com.github.teamfossilsarcheology.fossil.world.feature.neoforge;

import com.github.teamfossilsarcheology.fossil.FossilMod;
import com.github.teamfossilsarcheology.fossil.world.feature.ModFeatures;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.RegisterEvent;

import static com.github.teamfossilsarcheology.fossil.world.feature.ModFeatures.*;

/**
 * @see com.github.teamfossilsarcheology.fossil.world.feature.ModFeatures
 */
@EventBusSubscriber(modid = FossilMod.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ModFeaturesImpl {
    @SubscribeEvent
    public static void registerFeatures(RegisterEvent event) {
        register(event, ASH_DISK);
        register(event, CALAMITES_TREE);
        register(event, CORDAITES_TREE);
        register(event, MUTANT_TREE);
        register(event, PALM_TREE);
        register(event, SIGILLARIA_TREE);
        register(event, TEMPSKYA_TREE);
        register(event, MOAI_STATUE);
        register(event, VOLCANO_CONE);
        register(event, VOLCANO_VENT);
    }

    private static void register(RegisterEvent event, ModFeatures.Tuple<?, ?> tuple) {
        event.register(Registries.FEATURE, helper -> helper.register(tuple.location(), tuple.feature()));
    }

    public static void register() {
    }
}
