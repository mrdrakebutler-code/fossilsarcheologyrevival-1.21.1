package com.github.teamfossilsarcheology.fossil.recipe;

import com.github.teamfossilsarcheology.fossil.inventory.WorktableMenu;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.NotNull;

public class WorktableRecipeBuilder extends WithFuelRecipeBuilder {
    public WorktableRecipeBuilder(String modId, ItemLike itemInput, ItemLike itemFuel, ItemLike itemOutput) {
        this(modId, itemInput, itemFuel, itemOutput, WorktableMenu.DEFAULT_DURATION);
    }

    public WorktableRecipeBuilder(String modId, ItemLike itemInput, ItemLike itemFuel, ItemLike itemOutput, int duration) {
        super(modId, itemInput, itemFuel, itemOutput, duration);
    }

    @Override
    public void save(@NotNull RecipeOutput output, @NotNull ResourceLocation recipeId) {
        output.accept(recipeId, new WorktableRecipe(Ingredient.of(itemInput), Ingredient.of(itemFuel), new ItemStack(itemOutput), duration, WorktableMenu.DEFAULT_FUEL_DURATION), null);
    }

    @Override
    protected ResourceLocation getDefaultRecipeId() {
        return ResourceLocation.fromNamespaceAndPath(modId, "worktable/" + BuiltInRegistries.ITEM.getKey(itemInput.asItem()).getPath() + "_with_" + BuiltInRegistries.ITEM.getKey(itemFuel.asItem()).getPath());
    }
}
