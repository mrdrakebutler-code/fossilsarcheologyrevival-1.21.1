package com.github.teamfossilsarcheology.fossil.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public abstract class WithFuelRecipe implements Recipe<WithFuelRecipe.ContainerWithAnyFuel> {
    final Ingredient input;
    final Ingredient fuel;
    final ItemStack result;
    final int duration;
    final int fuelDuration;

    protected WithFuelRecipe(Ingredient input, Ingredient fuel, ItemStack result, int duration, int fuelDuration) {
        this.input = input;
        this.fuel = fuel;
        this.result = result;
        this.duration = duration;
        this.fuelDuration = fuelDuration;
    }

    @Override
    public boolean matches(ContainerWithAnyFuel container, Level level) {
        return input.test(container.getItem(0)) && (container.anyFuel || fuel.test(container.getItem(1)));
    }

    @Override
    public @NotNull ItemStack assemble(ContainerWithAnyFuel container, HolderLookup.Provider registries) {
        ItemStack itemStack = result.copy();
        itemStack.applyComponents(container.getItem(0).getComponents());
        return itemStack;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return false;
    }

    @Override
    public @NotNull ItemStack getResultItem(HolderLookup.Provider registries) {
        return result.copy();
    }

    @Override
    public @NotNull NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> nonNullList = NonNullList.create();
        nonNullList.add(input);
        return nonNullList;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    public boolean isFuel(ItemStack itemStack) {
        return fuel.test(itemStack);
    }

    public Ingredient getInput() {
        return input;
    }

    public Ingredient getFuel() {
        return fuel;
    }

    public int getDuration() {
        return duration;
    }

    public int getFuelDuration() {
        return fuelDuration;
    }

    public static class ContainerWithAnyFuel extends SimpleContainer implements RecipeInput {
        public final boolean anyFuel;

        public ContainerWithAnyFuel(boolean anyFuel, ItemStack... items) {
            super(items);
            this.anyFuel = anyFuel;
        }

        public ContainerWithAnyFuel(ItemStack input, ItemStack fuel) {
            super(input, fuel);
            this.anyFuel = false;
        }

        @Override
        public int size() {
            return getContainerSize();
        }
    }

    public static abstract class WithFuelRecipeSerializer<T extends WithFuelRecipe> implements RecipeSerializer<T> {
        protected final Constructor<T> constructor;

        protected WithFuelRecipeSerializer(Constructor<T> constructor) {
            this.constructor = constructor;
        }

        abstract int defaultDuration();

        abstract int defaultFuelDuration();

        @Override
        public @NotNull MapCodec<T> codec() {
            return RecordCodecBuilder.mapCodec(instance -> instance.group(
                    Ingredient.CODEC.fieldOf("input").forGetter(recipe -> recipe.input),
                    Ingredient.CODEC.fieldOf("fuel").forGetter(recipe -> recipe.fuel),
                    ItemStack.CODEC.fieldOf("result").forGetter(recipe -> recipe.result),
                    com.mojang.serialization.Codec.INT.optionalFieldOf("duration", defaultDuration()).forGetter(recipe -> recipe.duration),
                    com.mojang.serialization.Codec.INT.optionalFieldOf("fuel_duration", defaultFuelDuration()).forGetter(recipe -> recipe.fuelDuration)
            ).apply(instance, (input, fuel, result, duration, fuelDuration) -> constructor.construct(input, fuel, result, duration, fuelDuration)));
        }

        @Override
        public @NotNull StreamCodec<RegistryFriendlyByteBuf, T> streamCodec() {
            return StreamCodec.composite(
                    Ingredient.CONTENTS_STREAM_CODEC, recipe -> recipe.input,
                    Ingredient.CONTENTS_STREAM_CODEC, recipe -> recipe.fuel,
                    ItemStack.STREAM_CODEC, recipe -> recipe.result,
                    ByteBufCodecs.INT, recipe -> recipe.duration,
                    ByteBufCodecs.INT, recipe -> recipe.fuelDuration,
                    (input, fuel, result, duration, fuelDuration) -> constructor.construct(input, fuel, result, duration, fuelDuration)
            );
        }

        @FunctionalInterface
        public interface Constructor<R> {
            R construct(Ingredient input, Ingredient fuel, ItemStack output, int duration, int fuelDuration);
        }
    }
}
