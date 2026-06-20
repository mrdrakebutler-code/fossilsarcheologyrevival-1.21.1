package com.github.teamfossilsarcheology.fossil.recipe;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.NotNull;

public class SifterRecipeBuilder extends MultiOutputAndSlotsRecipeBuilder<SifterRecipeBuilder> {
    public SifterRecipeBuilder(String modId, ItemLike itemInput) {
        super(modId, itemInput);
    }

    public SifterRecipeBuilder(String modId, TagKey<Item> tagInput) {
        super(modId, tagInput);
    }

    @Override
    public void save(@NotNull RecipeOutput output, @NotNull ResourceLocation recipeId) {
        output.accept(recipeId, new SifterRecipe(buildIngredient(), buildOutputs()), null);
    }

    @Override
    protected ResourceLocation getDefaultRecipeId() {
        if (itemInput != null) {
            return ResourceLocation.fromNamespaceAndPath(modId, "sifter/" + BuiltInRegistries.ITEM.getKey(itemInput.asItem()).getPath());
        } else if (tagInput != null) {
            return ResourceLocation.fromNamespaceAndPath(modId, "sifter/" + tagInput.location().getPath());
        }
        return BuiltInRegistries.ITEM.getKey(Items.ENDER_PEARL);
    }
}
