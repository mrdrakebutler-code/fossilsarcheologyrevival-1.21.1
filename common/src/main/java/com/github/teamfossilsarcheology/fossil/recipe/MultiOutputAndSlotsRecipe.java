package com.github.teamfossilsarcheology.fossil.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.NavigableMap;
import java.util.TreeMap;

public abstract class MultiOutputAndSlotsRecipe implements Recipe<RecipeInput> {
    protected final Ingredient input;
    private final NavigableMap<Double, ItemStack> weightedOutputs;

    protected MultiOutputAndSlotsRecipe(Ingredient input, NavigableMap<Double, ItemStack> weightedOutputs) {
        this.input = input;
        this.weightedOutputs = weightedOutputs;
    }

    @Override
    public @NotNull NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> nonNullList = NonNullList.create();
        nonNullList.add(input);
        return nonNullList;
    }

    public Ingredient getInput() {
        return input;
    }

    public NavigableMap<Double, ItemStack> getWeightedOutputs() {
        return weightedOutputs;
    }

    @Override
    public boolean matches(RecipeInput container, Level level) {
        for (int i = 0; i < container.size(); i++) {
            ItemStack itemStack = container.getItem(i);
            if (!itemStack.isEmpty() && input.test(itemStack)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Picks one of the weighted outputs at random. Replaces the old {@code assemble} that relied on
     * casting the container to a {@link net.minecraft.world.level.block.entity.BlockEntity} to reach the level random.
     */
    public ItemStack getWeightedResult(RandomSource random) {
        return weightedOutputs.higherEntry(random.nextDouble() * weightedOutputs.lastKey()).getValue().copy();
    }

    @Override
    public @NotNull ItemStack assemble(RecipeInput container, HolderLookup.Provider registries) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return false;
    }

    @Override
    public @NotNull ItemStack getResultItem(HolderLookup.Provider registries) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    /**
     * Recovers the per-entry (non-cumulative) weights from the cumulative {@link #weightedOutputs} map so the
     * recipe can be re-serialized. The cumulative key of each entry minus the previous key is its own weight.
     */
    List<WeightedOutput> weightedOutputsAsList() {
        List<WeightedOutput> list = new ArrayList<>();
        double previous = 0;
        for (var entry : weightedOutputs.entrySet()) {
            list.add(new WeightedOutput(entry.getValue(), entry.getKey() - previous));
            previous = entry.getKey();
        }
        return list;
    }

    private static NavigableMap<Double, ItemStack> buildCumulativeMap(List<WeightedOutput> outputs) {
        NavigableMap<Double, ItemStack> map = new TreeMap<>();
        double total = 0;
        for (WeightedOutput output : outputs) {
            total += output.weight();
            map.put(total, output.item());
        }
        return map;
    }

    public record WeightedOutput(ItemStack item, double weight) {
        // OPTIONAL_CODEC (not CODEC): the "nothing" chance is represented by an empty/AIR ItemStack, which the
        // strict ItemStack.CODEC rejects (count must be 1-99, item must not be air).
        public static final Codec<WeightedOutput> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                ItemStack.OPTIONAL_CODEC.fieldOf("item").forGetter(WeightedOutput::item),
                Codec.DOUBLE.fieldOf("weight").forGetter(WeightedOutput::weight)
        ).apply(instance, WeightedOutput::new));
    }

    public static abstract class Serializer<T extends MultiOutputAndSlotsRecipe> implements RecipeSerializer<T> {
        protected final Constructor<T> constructor;

        protected Serializer(Constructor<T> constructor) {
            this.constructor = constructor;
        }

        @Override
        public @NotNull MapCodec<T> codec() {
            return RecordCodecBuilder.mapCodec(instance -> instance.group(
                    Ingredient.CODEC.fieldOf("input").forGetter(recipe -> recipe.input),
                    WeightedOutput.CODEC.listOf().fieldOf("outputs").forGetter(MultiOutputAndSlotsRecipe::weightedOutputsAsList)
            ).apply(instance, (input, outputs) -> constructor.construct(input, buildCumulativeMap(outputs))));
        }

        @Override
        public @NotNull StreamCodec<RegistryFriendlyByteBuf, T> streamCodec() {
            return StreamCodec.of(
                    (buffer, recipe) -> {
                        Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.input);
                        List<WeightedOutput> list = recipe.weightedOutputsAsList();
                        buffer.writeVarInt(list.size());
                        for (WeightedOutput output : list) {
                            buffer.writeDouble(output.weight());
                            ItemStack.OPTIONAL_STREAM_CODEC.encode(buffer, output.item());
                        }
                    },
                    buffer -> {
                        Ingredient input = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
                        int size = buffer.readVarInt();
                        List<WeightedOutput> list = new ArrayList<>();
                        for (int i = 0; i < size; i++) {
                            double weight = buffer.readDouble();
                            list.add(new WeightedOutput(ItemStack.OPTIONAL_STREAM_CODEC.decode(buffer), weight));
                        }
                        return constructor.construct(input, buildCumulativeMap(list));
                    }
            );
        }

        @FunctionalInterface
        public interface Constructor<R> {
            R construct(Ingredient input, NavigableMap<Double, ItemStack> outputs);
        }
    }
}
