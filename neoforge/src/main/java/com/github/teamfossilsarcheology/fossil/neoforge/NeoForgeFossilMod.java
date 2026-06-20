package com.github.teamfossilsarcheology.fossil.neoforge;

import com.github.teamfossilsarcheology.fossil.FossilMod;
import com.github.teamfossilsarcheology.fossil.advancements.ModTriggers;
import com.github.teamfossilsarcheology.fossil.block.entity.EnergyContainerBlockEntity;
import com.github.teamfossilsarcheology.fossil.block.entity.ModBlockEntities;
import com.github.teamfossilsarcheology.fossil.client.ClientInit;
import com.github.teamfossilsarcheology.fossil.config.FossilConfig;
import com.github.teamfossilsarcheology.fossil.config.neoforge.NeoForgeConfig;
import com.github.teamfossilsarcheology.fossil.config.neoforge.NeoForgeConfigFix;
import com.github.teamfossilsarcheology.fossil.entity.ModEntities;
import com.github.teamfossilsarcheology.fossil.entity.prehistoric.base.PrehistoricFish;
import com.github.teamfossilsarcheology.fossil.entity.prehistoric.fish.Coelacanth;
import com.github.teamfossilsarcheology.fossil.neoforge.capabilities.ModAttachments;
import com.github.teamfossilsarcheology.fossil.neoforge.world.biome.NeoForgeFossilRegion;
import com.github.teamfossilsarcheology.fossil.world.chunk.AnuLairChunkGenerator;
import com.github.teamfossilsarcheology.fossil.world.chunk.TreasureChunkGenerator;
import com.github.teamfossilsarcheology.fossil.world.feature.placement.ModPlacedFeatures;
import com.github.teamfossilsarcheology.fossil.world.feature.structures.ModStructureType;
import com.github.teamfossilsarcheology.fossil.world.surfacerules.ModSurfaceRules;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import net.neoforged.neoforge.items.wrapper.SidedInvWrapper;
import terrablender.api.RegionType;
import terrablender.api.Regions;
import terrablender.api.SurfaceRuleManager;

@Mod(FossilMod.MOD_ID)
public class NeoForgeFossilMod {

    public NeoForgeFossilMod(IEventBus modEventBus, ModContainer modContainer) {
        // architectury-neoforge (13.x) registers the mod event bus automatically; no manual EventBuses call needed.
        FossilMod.init();

        ModAttachments.ATTACHMENT_TYPES.register(modEventBus);

        modEventBus.addListener(this::onClient);
        modEventBus.addListener(this::onCommon);
        modEventBus.addListener(this::registerCapabilities);
        modEventBus.addListener(this::registerSpawnPlacements);
        modEventBus.addListener(this::onRegister);
        modEventBus.addListener(NeoForgeConfigFix::fixConfig);
        modContainer.registerConfig(ModConfig.Type.COMMON, NeoForgeConfig.COMMON_SPEC);
        modContainer.registerConfig(ModConfig.Type.CLIENT, NeoForgeConfig.CLIENT_SPEC);
        if (FMLEnvironment.dist == Dist.CLIENT) {
            ClientInit.immediate();
        }
    }

    public void onClient(FMLClientSetupEvent event) {
        ClientInit.later();
    }

    public void onCommon(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            // TODO(Phase 3 compat): re-enable Farmers Delight / Alex's Mobs food-mapping hooks once their
            //   1.21.1 NeoForge compat is ported (compat/** is currently excluded from compilation).
            ModPlacedFeatures.register();
            Regions.register(new NeoForgeFossilRegion("overworld", RegionType.OVERWORLD, 4));
            SurfaceRuleManager.addSurfaceRules(SurfaceRuleManager.RuleCategory.OVERWORLD, FossilMod.MOD_ID, ModSurfaceRules.VOLCANIC_SURFACE_RULE);
        });
    }

    // 1.21/NeoForge freezes the vanilla STRUCTURE_TYPE and CHUNK_GENERATOR registries right after the
    // RegisterEvent phase, so these direct Registry.register calls can no longer live in FMLCommonSetupEvent
    // (which fires post-freeze) as they did on 1.20.1/Forge — that crashed both runtime and datagen. Each
    // registry is open only during its own RegisterEvent firing, so register on the matching key.
    private void onRegister(RegisterEvent event) {
        if (Registries.STRUCTURE_TYPE.equals(event.getRegistryKey())) {
            ModStructureType.register(); // triggers the static Registry.register(STRUCTURE_TYPE, ...) calls
        } else if (Registries.TRIGGER_TYPE.equals(event.getRegistryKey())) {
            ModTriggers.register(); // triggers the static CriteriaTriggers.register(...) calls
        } else if (Registries.CHUNK_GENERATOR.equals(event.getRegistryKey())) {
            Registry.register(BuiltInRegistries.CHUNK_GENERATOR, FossilMod.location("treasure_room"), TreasureChunkGenerator.CODEC);
            Registry.register(BuiltInRegistries.CHUNK_GENERATOR, FossilMod.location("anu_lair"), AnuLairChunkGenerator.CODEC);
        }
    }

    // 1.21 made SpawnPlacements#register private; registration now flows through the mod-bus RegisterSpawnPlacementsEvent.
    private void registerSpawnPlacements(RegisterSpawnPlacementsEvent event) {
        event.register(ModEntities.ALLIGATOR_GAR.get(), SpawnPlacementTypes.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, PrehistoricFish::canSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(ModEntities.COELACANTH.get(), SpawnPlacementTypes.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Coelacanth::canCoelacanthSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(ModEntities.NAUTILUS.get(), SpawnPlacementTypes.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, PrehistoricFish::canSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(ModEntities.STURGEON.get(), SpawnPlacementTypes.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, PrehistoricFish::canSpawn, RegisterSpawnPlacementsEvent.Operation.REPLACE);
    }

    private void registerCapabilities(RegisterCapabilitiesEvent event) {
        // Sided item handlers for every machine (replaces the Forge getCapability mixins).
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ModBlockEntities.ANALYZER.get(),
                (be, side) -> side == null ? null : new SidedInvWrapper(be, side));
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ModBlockEntities.CULTURE_VAT.get(),
                (be, side) -> side == null ? null : new SidedInvWrapper(be, side));
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ModBlockEntities.SIFTER.get(),
                (be, side) -> side == null ? null : new SidedInvWrapper(be, side));
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ModBlockEntities.WORKTABLE.get(),
                (be, side) -> side == null ? null : new SidedInvWrapper(be, side));
        // Energy storage for the energy-backed machines (only when the config requires energy).
        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, ModBlockEntities.ANALYZER.get(),
                NeoForgeFossilMod::energyProvider);
        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, ModBlockEntities.CULTURE_VAT.get(),
                NeoForgeFossilMod::energyProvider);
    }

    private static IEnergyStorage energyProvider(EnergyContainerBlockEntity be, net.minecraft.core.Direction side) {
        if (side != null && FossilConfig.isEnabled(FossilConfig.MACHINES_REQUIRE_ENERGY)) {
            return (IEnergyStorage) be.getEnergyStorage();
        }
        return null;
    }
}
