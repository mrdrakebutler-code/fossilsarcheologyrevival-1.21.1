package com.github.teamfossilsarcheology.fossil.recipe;

import net.minecraft.advancements.Criterion;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class WithFuelRecipeBuilder implements RecipeBuilder {
    protected final String modId;
    protected final ItemLike itemInput;
    protected final ItemLike itemFuel;
    protected final ItemLike itemOutput;
    protected final int duration;

    protected WithFuelRecipeBuilder(String modId, ItemLike itemInput, ItemLike itemFuel, ItemLike itemOutput, int duration) {
        this.modId = modId;
        this.itemInput = itemInput;
        this.itemFuel = itemFuel;
        this.itemOutput = itemOutput;
        this.duration = duration;
    }

    @Override
    public @NotNull RecipeBuilder unlockedBy(@NotNull String criterionName, @NotNull Criterion<?> criterionTrigger) {
        return this;
    }

    @Override
    public @NotNull RecipeBuilder group(@Nullable String groupName) {
        return this;
    }

    @Override
    public @NotNull Item getResult() {
        //Method is unused
        return itemOutput.asItem();
    }

    @Override
    public void save(@NotNull RecipeOutput output) {
        save(output, getDefaultRecipeId());
    }

    protected abstract ResourceLocation getDefaultRecipeId();
}
