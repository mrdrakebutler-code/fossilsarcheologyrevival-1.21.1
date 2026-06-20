package com.github.teamfossilsarcheology.fossil.neoforge.data.providers;

import com.github.teamfossilsarcheology.fossil.FossilMod;
import com.github.teamfossilsarcheology.fossil.entity.damagesource.ModDamageTypes;
import com.github.teamfossilsarcheology.fossil.neoforge.world.biome.NeoForgeFossilBiomeModifiers;
import com.github.teamfossilsarcheology.fossil.world.biome.ModBiomes;
import com.github.teamfossilsarcheology.fossil.world.dimension.ModDimensions;
import com.github.teamfossilsarcheology.fossil.world.feature.configuration.ModConfiguredFeatures;
import com.github.teamfossilsarcheology.fossil.world.feature.placement.ModPlacedFeatures;
import com.github.teamfossilsarcheology.fossil.world.feature.structures.ModStructureSets;
import com.github.teamfossilsarcheology.fossil.world.feature.structures.ModStructures;
import com.github.teamfossilsarcheology.fossil.world.feature.structures.ModTemplatePools;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class ModWorldGenProvider extends DatapackBuiltinEntriesProvider {
    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.ENCHANTMENT, ModEnchantmentGen::bootstrap)
            .add(Registries.CONFIGURED_FEATURE, ModConfiguredFeatures::bootstrap)
            .add(Registries.PLACED_FEATURE, ModPlacedFeatures::bootstrap)
            .add(Registries.STRUCTURE, ModStructures::bootstrap)
            .add(Registries.STRUCTURE_SET, ModStructureSets::bootstrap)
            .add(Registries.BIOME, ModBiomes::bootstrap)
            .add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, NeoForgeFossilBiomeModifiers::bootstrap)
            .add(Registries.TEMPLATE_POOL, ModTemplatePools::bootstrap)
            .add(Registries.DIMENSION_TYPE, ModDimensions::bootstrap)
            .add(Registries.DAMAGE_TYPE, ModDamageTypes::bootstrap);

    public ModWorldGenProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(FossilMod.MOD_ID));
    }
}
