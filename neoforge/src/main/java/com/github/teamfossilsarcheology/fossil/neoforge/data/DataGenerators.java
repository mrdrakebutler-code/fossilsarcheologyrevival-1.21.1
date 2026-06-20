package com.github.teamfossilsarcheology.fossil.neoforge.data;

import com.github.teamfossilsarcheology.fossil.FossilMod;
import com.github.teamfossilsarcheology.fossil.neoforge.data.advancements.FossilAdvancements;
import com.github.teamfossilsarcheology.fossil.neoforge.data.loot.ModLootProvider;
import com.github.teamfossilsarcheology.fossil.neoforge.data.providers.ModBlockStateProvider;
import com.github.teamfossilsarcheology.fossil.neoforge.data.providers.ModBlockTagsProvider;
import com.github.teamfossilsarcheology.fossil.neoforge.data.providers.ModItemProvider;
import com.github.teamfossilsarcheology.fossil.neoforge.data.providers.ModEntityTypeTagsProvider;
import com.github.teamfossilsarcheology.fossil.neoforge.data.providers.ModItemTagsProvider;
import com.github.teamfossilsarcheology.fossil.neoforge.data.providers.ModFoodValueProvider;
import com.github.teamfossilsarcheology.fossil.neoforge.data.providers.ModRecipeProvider;
import com.github.teamfossilsarcheology.fossil.neoforge.data.providers.ModWorldGenProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.AdvancementProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = FossilMod.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class DataGenerators {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookup = event.getLookupProvider();
        ExistingFileHelper efh = event.getExistingFileHelper();

        // Client assets: block models + blockstates (+ block-item models) and item models. ModBlockStateProvider
        // owns the ModBlockModelProvider/ModItemProvider instances it generates through, so it must run alongside.
        generator.addProvider(event.includeClient(), new ModBlockStateProvider(output, efh));
        generator.addProvider(event.includeClient(), new ModItemProvider(output, efh));

        generator.addProvider(event.includeServer(), new ModRecipeProvider(output, lookup));
        ModWorldGenProvider worldGen = generator.addProvider(event.includeServer(), new ModWorldGenProvider(output, lookup));
        // Enriched lookup includes the mod's datapack registries (notably ENCHANTMENT) so downstream providers can
        // resolve fossil:archeology / fossil:paleontology Holders — the block loot tables need them.
        CompletableFuture<HolderLookup.Provider> enriched = worldGen.getRegistryProvider();

        ModBlockTagsProvider blockTags = generator.addProvider(event.includeServer(), new ModBlockTagsProvider(output, lookup, efh));
        generator.addProvider(event.includeServer(), new ModItemTagsProvider(output, lookup, blockTags.contentsGetter(), efh));
        generator.addProvider(event.includeServer(), new ModEntityTypeTagsProvider(output, lookup, efh));

        generator.addProvider(event.includeServer(), new ModLootProvider(output, enriched));
        generator.addProvider(event.includeServer(), new ModFoodValueProvider(output));

        generator.addProvider(event.includeServer(), new AdvancementProvider(output, lookup, efh,
                List.of((registries, writer, existingFileHelper) -> new FossilAdvancements().accept(writer))));
    }
}
