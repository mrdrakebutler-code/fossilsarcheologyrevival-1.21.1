package com.github.teamfossilsarcheology.fossil.item;

import com.github.teamfossilsarcheology.fossil.entity.prehistoric.base.PrehistoricEntityInfo;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public enum ModToolTiers implements Tier {
    // 1.21: mining "level" int replaced by an "incorrect blocks for drops" tag. Old level 4 ~ netherite, level 3 ~ diamond.
    SCARAB(BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 1861, 9, 5, 25, Ingredient::of),
    TOOTH_DAGGER(BlockTags.INCORRECT_FOR_DIAMOND_TOOL, 250, 6.4f, 2, 25, () -> Ingredient.of(PrehistoricEntityInfo.TYRANNOSAURUS.uniqueBoneItem));

    private final TagKey<Block> incorrectBlocksForDrops;
    private final int uses;
    private final float speed;
    private final float damage;
    private final int enchantmentValue;
    private final LazyLoadedValue<Ingredient> repairIngredient;

    ModToolTiers(TagKey<Block> incorrectBlocksForDrops, int uses, float speed, float damage, int enchantment, Supplier<Ingredient> supplier) {
        this.incorrectBlocksForDrops = incorrectBlocksForDrops;
        this.uses = uses;
        this.speed = speed;
        this.damage = damage;
        this.enchantmentValue = enchantment;
        this.repairIngredient = new LazyLoadedValue<>(supplier);
    }

    @Override
    public int getUses() {
        return this.uses;
    }

    @Override
    public float getSpeed() {
        return this.speed;
    }

    @Override
    public float getAttackDamageBonus() {
        return this.damage;
    }

    @Override
    public @NotNull TagKey<Block> getIncorrectBlocksForDrops() {
        return this.incorrectBlocksForDrops;
    }

    @Override
    public int getEnchantmentValue() {
        return this.enchantmentValue;
    }

    @Override
    public @NotNull Ingredient getRepairIngredient() {
        return this.repairIngredient.get();
    }
}
