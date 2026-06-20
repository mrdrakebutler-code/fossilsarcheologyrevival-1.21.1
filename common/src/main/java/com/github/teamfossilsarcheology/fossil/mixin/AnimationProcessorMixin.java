package com.github.teamfossilsarcheology.fossil.mixin;

import com.github.teamfossilsarcheology.fossil.client.model.AdditiveAnimationModel;
import com.github.teamfossilsarcheology.fossil.compat.geckolib.AnimationProcessorOverride;
import com.github.teamfossilsarcheology.fossil.entity.prehistoric.base.PrehistoricAnimatable;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimationProcessor;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.state.BoneSnapshot;

import java.util.Map;

@Mixin(AnimationProcessor.class)
public class AnimationProcessorMixin<T extends GeoAnimatable> {

    @Shadow
    @Final
    private Map<String, GeoBone> bones;

    @Shadow
    @Final
    private GeoModel<T> model;

    @Shadow
    public boolean reloadAnimations;

    // TODO(Phase 5 geckolib): re-target for GeckoLib 4.7 — AnimatableManager moved from core.animation.* to
    //  animation.*, so the old "core/animation/AnimatableManager" INVOKE target scans 0 targets. require=0 makes
    //  these two injections no-ops (additive-animation override disabled) instead of crashing mod load/datagen.
    @Inject(method = "tickAnimation", remap = false, require = 0, at = @At(value = "INVOKE", target = "Lsoftware/bernie/geckolib/core/animation/AnimatableManager;getAnimationControllers()Ljava/util/Map;"))
    public void tickAdditiveAnimations(T animatable, GeoModel<T> model, AnimatableManager<T> animatableManager, double animTime, AnimationState<T> state, boolean crashWhenCantFindBone, CallbackInfo ci,
                                       @Local AnimatableManager<T> manager, @Local(name = "boneSnapshots") Map<String, BoneSnapshot> boneSnapshots) {
        if (animatable instanceof PrehistoricAnimatable<?>) {
            AnimationProcessorOverride.tickAdditiveAnimations(animatable, model, animTime, state, reloadAnimations, bones, crashWhenCantFindBone, manager, boneSnapshots);
        }
    }

    @ModifyExpressionValue(method = "tickAnimation", remap = false, require = 0, at = @At(value = "INVOKE", target = "Lsoftware/bernie/geckolib/core/animation/AnimatableManager;getAnimationControllers()Ljava/util/Map;"))
    public Map<String, AnimationController<T>> cancelDefaultAnimations(Map<String, AnimationController<T>> original) {
        if (model instanceof AdditiveAnimationModel) {
            return Map.of();
        }
        return original;
    }
}
