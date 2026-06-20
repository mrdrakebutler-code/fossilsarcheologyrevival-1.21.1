package com.github.teamfossilsarcheology.fossil.advancements;

import com.github.teamfossilsarcheology.fossil.item.ModItems;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * Triggers whenever a {@link com.github.teamfossilsarcheology.fossil.entity.prehistoric.base.Prehistoric Prehistoric} is tamed with a {@link ModItems#SCARAB_GEM}
 */
public class ScarabTameTrigger extends SimpleCriterionTrigger<ScarabTameTrigger.TriggerInstance> {

    public void trigger(ServerPlayer player) {
        trigger(player, triggerInstance -> true);
    }

    @Override
    public @NotNull Codec<TriggerInstance> codec() {
        return TriggerInstance.CODEC;
    }

    public record TriggerInstance(Optional<ContextAwarePredicate> player) implements SimpleCriterionTrigger.SimpleInstance {
        public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player)
        ).apply(instance, TriggerInstance::new));

        public static TriggerInstance scarabTame() {
            return new TriggerInstance(Optional.empty());
        }
    }
}
