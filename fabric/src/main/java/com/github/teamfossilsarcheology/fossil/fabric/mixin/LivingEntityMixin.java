package com.github.teamfossilsarcheology.fossil.fabric.mixin;

import com.github.teamfossilsarcheology.fossil.material.ModFluids;
import com.github.teamfossilsarcheology.fossil.world.effect.ModEffects;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    protected LivingEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @ModifyExpressionValue(method = "baseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;isEyeInFluid(Lnet/minecraft/tags/TagKey;)Z"))
    public boolean test(boolean original) {
        return original || isEyeInFluid(ModFluids.TAR_FLUID);
    }

    @Inject(method = "getVisibilityPercent", at = @At("RETURN"), cancellable = true)
    public void getVisibilityPercent(Entity lookingEntity, CallbackInfoReturnable<Double> cir) {
        if (((LivingEntity) (Object) this).hasEffect(ModEffects.COMFY_BED.get())) {
            cir.setReturnValue(cir.getReturnValue() * 0.5);
        }
    }
}
