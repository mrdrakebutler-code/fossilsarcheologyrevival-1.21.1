package com.github.teamfossilsarcheology.fossil.neoforge.client;

import com.github.teamfossilsarcheology.fossil.FossilMod;
import com.github.teamfossilsarcheology.fossil.client.model.block.PlantBlockModel;
import com.github.teamfossilsarcheology.fossil.material.ModFluids;
import com.github.teamfossilsarcheology.fossil.neoforge.client.model.PlantModelLoader;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;

@EventBusSubscriber(modid = FossilMod.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class ClientModBusEvents {

    @SubscribeEvent
    public static void onRegisterGeometryLoaders(ModelEvent.RegisterGeometryLoaders event) {
        event.register(PlantBlockModel.LOADER, new PlantModelLoader());
    }

    /**
     * 1.21.1 / NeoForge: register the tar fluid's client (texture) extension manually.
     * <p>
     * architectury's {@code ArchitecturyFlowingFluid} creates its {@link net.neoforged.neoforge.fluids.FluidType}
     * lazily into a private map and never registers it in {@code NeoForgeRegistries.FLUID_TYPES}. On Forge 1.20.1 that
     * was fine, but NeoForge 1.21.1 gathers fluid {@link IClientFluidTypeExtensions} up front by iterating the
     * FLUID_TYPES registry ({@code ClientExtensionsManager.earlyInit}) — so the tar type's {@code initializeClient} is
     * never called, its extension never lands in the lookup, and {@code IClientFluidTypeExtensions.of(tar)} falls back
     * to DEFAULT whose {@code getStillTexture()} returns {@code null}. That null then NPEs in
     * {@code FluidSpriteCache.getFluidSprites} the moment a tar block is tesselated.
     * <p>
     * Registering the extension here (delegating to the same {@link ModFluids#TAR_ATTRIBUTES} textures) restores the
     * tar still/flowing sprites. TAR and TAR_FLOWING share ONE FluidType instance (architectury keys it by attributes
     * identity via its internal FLUID_TYPE_MAP), so we register that single type exactly once — passing it twice trips
     * NeoForge's "Duplicate client extensions registration" guard.
     */
    @SubscribeEvent
    public static void onRegisterClientExtensions(RegisterClientExtensionsEvent event) {
        event.registerFluidType(new IClientFluidTypeExtensions() {
            @Override
            public ResourceLocation getStillTexture() {
                return ModFluids.TAR_ATTRIBUTES.getSourceTexture();
            }

            @Override
            public ResourceLocation getFlowingTexture() {
                return ModFluids.TAR_ATTRIBUTES.getFlowingTexture();
            }

            @Override
            public ResourceLocation getOverlayTexture() {
                return ModFluids.TAR_ATTRIBUTES.getOverlayTexture();
            }
        }, ModFluids.TAR.get().getFluidType());
    }
}
