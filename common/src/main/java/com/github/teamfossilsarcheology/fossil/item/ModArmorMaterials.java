package com.github.teamfossilsarcheology.fossil.item;

import com.github.teamfossilsarcheology.fossil.FossilMod;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * 1.21: {@link ArmorMaterial} is now a record (no longer an interface with a durability getter), and
 * {@link ArmorItem} takes a {@code Holder<ArmorMaterial>}. We register our materials in the
 * {@link Registries#ARMOR_MATERIAL} registry and expose the resulting {@link RegistrySupplier}
 * (which is itself a {@link Holder}) plus a helper for per-slot durability, which moved onto the
 * item's {@link net.minecraft.world.item.Item.Properties}.
 */
public class ModArmorMaterials {
    public static final DeferredRegister<ArmorMaterial> ARMOR_MATERIALS = DeferredRegister.create(FossilMod.MOD_ID, Registries.ARMOR_MATERIAL);

    private static final EnumMap<ArmorItem.Type, Integer> HEALTH_FUNCTION_FOR_TYPE = Util.make(new EnumMap<>(ArmorItem.Type.class), enumMap -> {
        enumMap.put(ArmorItem.Type.BOOTS, 13);
        enumMap.put(ArmorItem.Type.LEGGINGS, 15);
        enumMap.put(ArmorItem.Type.CHESTPLATE, 16);
        enumMap.put(ArmorItem.Type.HELMET, 11);
    });

    public static final RegistrySupplier<ArmorMaterial> ANCIENT = ARMOR_MATERIALS.register("ancient", () -> new ArmorMaterial(
            Util.make(new EnumMap<>(ArmorItem.Type.class), enumMap -> {
                enumMap.put(ArmorItem.Type.BOOTS, 2);
                enumMap.put(ArmorItem.Type.LEGGINGS, 5);
                enumMap.put(ArmorItem.Type.CHESTPLATE, 6);
                enumMap.put(ArmorItem.Type.HELMET, 2);
            }),
            9,
            SoundEvents.ARMOR_EQUIP_CHAIN,
            () -> Ingredient.of(ModItems.SCARAB_GEM.get()),
            List.of(),
            0,
            0));

    public static final RegistrySupplier<ArmorMaterial> BONE = ARMOR_MATERIALS.register("bone", () -> new ArmorMaterial(
            Util.make(new EnumMap<>(ArmorItem.Type.class), enumMap -> {
                enumMap.put(ArmorItem.Type.BOOTS, 2);
                enumMap.put(ArmorItem.Type.LEGGINGS, 7);
                enumMap.put(ArmorItem.Type.CHESTPLATE, 6);
                enumMap.put(ArmorItem.Type.HELMET, 2);
            }),
            15,
            SoundEvents.ARMOR_EQUIP_CHAIN,
            () -> Ingredient.of(Items.BONE),
            List.of(),
            0,
            0));

    // 1.21: armor durability is no longer carried by ArmorMaterial; it must be set on Item.Properties.
    private static final Map<RegistrySupplier<ArmorMaterial>, Integer> DURABILITY_MULTIPLIER = Map.of(ANCIENT, 15, BONE, 25);

    /**
     * @return the durability for the given material + slot, to be passed to {@code Item.Properties.durability(...)}.
     */
    public static int durability(RegistrySupplier<ArmorMaterial> material, ArmorItem.Type type) {
        return HEALTH_FUNCTION_FOR_TYPE.get(type) * DURABILITY_MULTIPLIER.getOrDefault(material, 1);
    }

    public static void register() {
        ARMOR_MATERIALS.register();
    }
}
