package com.github.teamfossilsarcheology.fossil.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.EasingType;

import java.util.function.Function;

@Mixin(AnimationController.class)
public interface AnimationControllerAccessor<T extends GeoAnimatable> {

    @Accessor
    void setIsJustStarting(boolean isJustStarting);

    @Accessor("overrideEasingTypeFunction")
    Function<T, EasingType> getOverrideEasingTypeFunction();
}
