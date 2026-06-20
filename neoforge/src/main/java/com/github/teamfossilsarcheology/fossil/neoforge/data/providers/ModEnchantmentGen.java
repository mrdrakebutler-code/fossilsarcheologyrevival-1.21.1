package com.github.teamfossilsarcheology.fossil.neoforge.data.providers;

import com.github.teamfossilsarcheology.fossil.FossilMod;
import com.github.teamfossilsarcheology.fossil.enchantment.ModEnchantments;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;

/**
 * Datagen bootstrap for the mod's data-driven enchantments (1.21). Generating them here (rather than only as
 * hand-authored JSON) also makes their {@code Holder<Enchantment>}s resolvable from the datagen lookup, which
 * the block loot tables need for the archeology/paleontology fossil-drop conditions. Values mirror the original
 * hand-authored archeology.json/paleontology.json; the description id auto-derives to {@code enchantment.fossil.<name>}.
 */
public class ModEnchantmentGen {
    private static final TagKey<Enchantment> DIGGING = TagKey.create(Registries.ENCHANTMENT, FossilMod.location("exclusive_set/digging"));

    public static void bootstrap(BootstrapContext<Enchantment> context) {
        HolderGetter<Item> items = context.lookup(Registries.ITEM);
        HolderGetter<Enchantment> enchantments = context.lookup(Registries.ENCHANTMENT);
        register(context, items, enchantments, ModEnchantments.ARCHEOLOGY);
        register(context, items, enchantments, ModEnchantments.PALEONTOLOGY);
    }

    private static void register(BootstrapContext<Enchantment> context, HolderGetter<Item> items,
                                 HolderGetter<Enchantment> enchantments, ResourceKey<Enchantment> key) {
        context.register(key, Enchantment.enchantment(Enchantment.definition(
                        items.getOrThrow(ItemTags.MINING_ENCHANTABLE), 1, 3,
                        Enchantment.dynamicCost(1, 10), Enchantment.dynamicCost(51, 10), 8,
                        EquipmentSlotGroup.MAINHAND))
                .exclusiveWith(enchantments.getOrThrow(DIGGING))
                .build(key.location()));
    }
}
