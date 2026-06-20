package com.github.teamfossilsarcheology.fossil.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import software.bernie.geckolib.animation.AnimatableManager;

@Mixin(AnimatableManager.class)
public interface AnimatableManagerAccessor {

    @Accessor("isFirstTick")
    boolean isFirstTick();
}
