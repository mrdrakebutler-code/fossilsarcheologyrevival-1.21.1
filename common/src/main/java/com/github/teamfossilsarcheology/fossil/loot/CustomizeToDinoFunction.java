package com.github.teamfossilsarcheology.fossil.loot;

import com.github.teamfossilsarcheology.fossil.entity.prehistoric.base.Prehistoric;
import com.github.teamfossilsarcheology.fossil.tags.ModItemTags;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Changes the amount of food a {@link Prehistoric} drops based on age
 */
public class CustomizeToDinoFunction extends LootItemConditionalFunction {
    /**
     * 1.21: loot functions are codec-based. {@code LootItemFunctionType} wraps a {@link MapCodec}
     * instead of a {@code Serializer}. {@code commonFields(instance)} provides the shared conditions
     * list; we add our single {@code entity} field.
     */
    public static final MapCodec<CustomizeToDinoFunction> CODEC = RecordCodecBuilder.mapCodec(instance ->
            commonFields(instance).and(
                    LootContext.EntityTarget.CODEC.fieldOf("entity").forGetter(f -> f.entityTarget)
            ).apply(instance, CustomizeToDinoFunction::new));

    private final LootContext.EntityTarget entityTarget;

    protected CustomizeToDinoFunction(List<LootItemCondition> lootItemConditions, LootContext.EntityTarget entityTarget) {
        super(lootItemConditions);
        this.entityTarget = entityTarget;
    }

    public static LootItemConditionalFunction.Builder<?> apply(LootContext.EntityTarget target) {
        return simpleBuilder(lootItemConditions -> new CustomizeToDinoFunction(lootItemConditions, target));
    }

    @Override
    protected @NotNull ItemStack run(ItemStack stack, LootContext context) {
        if (!stack.isEmpty()) {
            Entity entity = context.getParamOrNull(entityTarget.getParam());
            if (entity instanceof Prehistoric prehistoric) {
                if (stack.is(ModItemTags.UNCOOKED_MEAT)) {
                    stack.setCount(Math.min(prehistoric.getAgeInDays(), prehistoric.data().adultAgeDays()));
                    return prehistoric.isOnFire() ? new ItemStack(prehistoric.info().cookedFoodItem, stack.getCount()) : stack;
                }
            }
        }
        return stack;
    }

    @Override
    public @NotNull LootItemFunctionType<CustomizeToDinoFunction> getType() {
        return ModLootItemFunctionTypes.CUSTOMIZE_TO_DINOSAUR.get();
    }
}
