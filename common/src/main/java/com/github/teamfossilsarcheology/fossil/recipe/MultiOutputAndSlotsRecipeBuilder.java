package com.github.teamfossilsarcheology.fossil.recipe;

import net.minecraft.advancements.Criterion;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public abstract class MultiOutputAndSlotsRecipeBuilder<T extends MultiOutputAndSlotsRecipeBuilder<T>> implements RecipeBuilder {
    protected final String modId;
    protected final ItemLike itemInput;
    protected final TagKey<Item> tagInput;
    protected final NavigableMap<ItemHolder, Double> weightedOutputs = new TreeMap<>();
    protected double total;
    protected double nothingWeight;

    protected MultiOutputAndSlotsRecipeBuilder(String modId, ItemLike itemInput) {
        this.modId = modId;
        this.itemInput = itemInput;
        this.tagInput = null;
    }

    protected MultiOutputAndSlotsRecipeBuilder(String modId, TagKey<Item> tagInput) {
        this.modId = modId;
        this.itemInput = null;
        this.tagInput = tagInput;
    }

    public MultiOutputAndSlotsRecipeBuilder<T> addOutput(ItemLike itemLike, double weight) {
        return addOutput(itemLike, 1, weight);
    }

    public MultiOutputAndSlotsRecipeBuilder<T> addOutput(ItemLike itemLike, int count, double weight) {
        total += weight;
        weightedOutputs.put(new ItemHolder(BuiltInRegistries.ITEM.getKey(itemLike.asItem()), count), weight);
        return this;
    }

    public MultiOutputAndSlotsRecipeBuilder<T> nothing(double weight) {
        nothingWeight = weight;
        return this;
    }

    public double getTotal() {
        return total + nothingWeight;
    }

    @Override
    public @NotNull RecipeBuilder unlockedBy(String criterionName, Criterion<?> criterionTrigger) {
        return this;
    }

    @Override
    public @NotNull RecipeBuilder group(@Nullable String groupName) {
        return this;
    }

    @Override
    public @NotNull Item getResult() {
        //Method is unused
        return Items.ENDER_PEARL;
    }

    @Override
    public void save(@NotNull net.minecraft.data.recipes.RecipeOutput output) {
        save(output, getDefaultRecipeId());
    }

    protected abstract ResourceLocation getDefaultRecipeId();

    protected Ingredient buildIngredient() {
        return itemInput != null ? Ingredient.of(itemInput) : Ingredient.of(tagInput);
    }

    /**
     * Builds the cumulative-weight map the runtime recipe expects from the per-entry weights collected by the builder,
     * appending an {@link Items#AIR} entry for the "nothing" chance when present.
     */
    protected NavigableMap<Double, ItemStack> buildOutputs() {
        NavigableMap<Double, ItemStack> map = new TreeMap<>();
        double accumulated = 0;
        for (Map.Entry<ItemHolder, Double> entry : weightedOutputs.entrySet()) {
            accumulated += entry.getValue();
            ItemHolder holder = entry.getKey();
            map.put(accumulated, new ItemStack(BuiltInRegistries.ITEM.get(holder.location()), holder.count()));
        }
        if (nothingWeight > 0) {
            accumulated += nothingWeight;
            map.put(accumulated, new ItemStack(Items.AIR));
        }
        return map;
    }

    public record ItemHolder(ResourceLocation location, int count) implements Comparable<ItemHolder> {

        @Override
        public int compareTo(@NotNull ItemHolder o) {
            return location.getPath().compareTo(o.location.getPath());
        }
    }
}
