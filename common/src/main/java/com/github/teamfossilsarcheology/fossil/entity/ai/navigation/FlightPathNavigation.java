package com.github.teamfossilsarcheology.fossil.entity.ai.navigation;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.pathfinder.FlyNodeEvaluator;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.level.pathfinder.PathfindingContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.Set;

public class FlightPathNavigation extends FlyingPathNavigation {
    public FlightPathNavigation(Mob mob, Level level) {
        super(mob, level);
    }

    @Override
    protected @NotNull PathFinder createPathFinder(int maxVisitedNodes) {
        nodeEvaluator = new FlightNodeEvaluator();
        return new PathFinder(nodeEvaluator, maxVisitedNodes);
    }

    @Nullable
    @Override
    protected Path createPath(Set<BlockPos> targets, int regionOffset, boolean offsetUpward, int accuracy, float followRange) {
        return CenteredPath.createFromPath(super.createPath(targets, regionOffset, offsetUpward, accuracy, followRange));
    }

    private static class FlightNodeEvaluator extends FlyNodeEvaluator {
        @Override
        public Set<PathType> getPathTypeWithinMobBB(PathfindingContext context, int x, int y, int z) {
            EnumSet<PathType> nodeTypeEnum = EnumSet.noneOf(PathType.class);
            float width = Math.max(0, entityWidth - 2);
            int widthEachSide = Mth.ceil(width / 2.0f) + 1;
            for (int i = 0; i < widthEachSide; ++i) {
                for (int j = 0; j < entityHeight; ++j) {
                    for (int k = 0; k < widthEachSide; ++k) {
                        nodeTypeEnum.add(this.getPathType(context, x + i, y + j, z + k));
                        if (i != 0 || k != 0) {
                            nodeTypeEnum.add(this.getPathType(context, x - i, y + j, z - k));
                        }
                    }
                }
            }
            return nodeTypeEnum;
        }
    }
}
