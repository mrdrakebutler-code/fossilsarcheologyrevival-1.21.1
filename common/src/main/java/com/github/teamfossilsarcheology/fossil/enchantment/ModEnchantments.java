package com.github.teamfossilsarcheology.fossil.enchantment;

import com.github.teamfossilsarcheology.fossil.FossilMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;

/**
 * 1.21: enchantments are fully data-driven. They can no longer be subclassed or registered as code
 * objects (the old {@code ArcheologyEnchantment}/{@code PaleontologyEnchantment} subclasses and the
 * {@code EnchantmentCategory} ctor are gone). Code only holds a {@link ResourceKey}; the actual
 * definition (max level 3, cost {@code 1 + 10*(lvl-1)} .. {@code +50}, slot MAINHAND, rarity
 * VERY_RARE, mutually exclusive with each other + {@code minecraft:silk_touch}) lives in datapack
 * JSON under {@code data/fossil/enchantment/} and is generated in Phase 4 datagen.
 * Resolve a {@code Holder<Enchantment>} at runtime via
 * {@code registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(KEY)}.
 */
public class ModEnchantments {
    public static final ResourceKey<Enchantment> ARCHEOLOGY = key("archeology");
    public static final ResourceKey<Enchantment> PALEONTOLOGY = key("paleontology");

    private static ResourceKey<Enchantment> key(String name) {
        return ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.fromNamespaceAndPath(FossilMod.MOD_ID, name));
    }

    public static void register() {
        // No-op: data-driven enchantments are provided by the datapack, not registered in code.
        // Kept so existing FossilMod.register() call sites stay valid.
    }
}
