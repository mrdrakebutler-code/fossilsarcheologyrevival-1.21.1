package com.github.teamfossilsarcheology.fossil.loot;

import com.github.teamfossilsarcheology.fossil.FossilMod;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;

public class ModLootItemFunctionTypes {

    private static final DeferredRegister<LootItemFunctionType<?>> LOOT_FUNCTION_TYPES = DeferredRegister.create(FossilMod.MOD_ID, Registries.LOOT_FUNCTION_TYPE);

    public static final RegistrySupplier<LootItemFunctionType<CustomizeToDinoFunction>> CUSTOMIZE_TO_DINOSAUR =
            LOOT_FUNCTION_TYPES.register("customize_to_dinosaur", () -> new LootItemFunctionType<>(CustomizeToDinoFunction.CODEC));

    public static void register() {
        LOOT_FUNCTION_TYPES.register();
    }
}
