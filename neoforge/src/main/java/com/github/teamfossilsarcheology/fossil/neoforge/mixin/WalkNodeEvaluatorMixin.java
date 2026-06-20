package com.github.teamfossilsarcheology.fossil.neoforge.mixin;

import com.github.teamfossilsarcheology.fossil.block.ModBlocks;
import com.github.teamfossilsarcheology.fossil.entity.prehistoric.base.Prehistoric;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.NodeEvaluator;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WalkNodeEvaluator.class)
public abstract class WalkNodeEvaluatorMixin extends NodeEvaluator {

    // 1.21 removed WalkNodeEvaluator#isBurningBlock; danger classification now flows through getPathTypeFromState,
    // so mark tar as a fire-damage path here to keep vanilla mobs avoiding tar pits.
    // TODO(Phase 5): verify pathing behaviour around tar at runtime.
    @Inject(method = "getPathTypeFromState", at = @At("RETURN"), cancellable = true)
    private static void addTarAsDangerousPath(BlockGetter level, BlockPos pos, CallbackInfoReturnable<PathType> cir) {
        if (ModBlocks.TAR.isPresent() && level.getBlockState(pos).is(ModBlocks.TAR.get())) {
            cir.setReturnValue(PathType.DAMAGE_FIRE);
        }
    }

    @WrapOperation(method = "findAcceptedNode", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/pathfinder/WalkNodeEvaluator;canReachWithoutCollision(Lnet/minecraft/world/level/pathfinder/Node;)Z"))
    public boolean preventMobFromGettingStuck(WalkNodeEvaluator instance, Node node, Operation<Boolean> original) {
        boolean collides = !original.call(instance, node);
        if (collides && mob instanceof Prehistoric && mob.blockPosition().equals(node.asBlockPos())) {
            //Mobs with certain BB widths ie 1-1.3 etc will get stuck in fence corners. See Trello
            return false;
        }
        return collides;
    }
}
