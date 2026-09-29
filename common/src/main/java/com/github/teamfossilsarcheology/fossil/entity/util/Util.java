package com.github.teamfossilsarcheology.fossil.entity.util;

import com.github.teamfossilsarcheology.fossil.entity.data.Attribute;
import com.github.teamfossilsarcheology.fossil.entity.data.EntityDataLoader;
import com.github.teamfossilsarcheology.fossil.entity.prehistoric.base.Prehistoric;
import com.github.teamfossilsarcheology.fossil.entity.prehistoric.base.SwimmingAnimal;
import com.github.teamfossilsarcheology.fossil.tags.ModBlockTags;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.util.Mth;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

public class Util {
    public static final int IMMOBILE = 0;
    public static final int ATTACK = 5;
    public static final int SLEEP = 10;
    public static final int NEEDS = 15;
    public static final int WANDER = 20;
    public static final int LOOK = 25;
    public static final float SWING_ANIM_THRESHOLD = 0.008f;

    public static void spawnParticles(Level level, ParticleOptions particleOptions, int count, AABB aabb) {
        for (int i = 0; i < count; i++) {
            double motionX = level.getRandom().nextGaussian() * 0.07;
            double motionY = level.getRandom().nextGaussian() * 0.07;
            double motionZ = level.getRandom().nextGaussian() * 0.07;
            float x = (float) (level.getRandom().nextFloat() * (aabb.maxX - aabb.minX) + aabb.minX);
            float y = (float) (level.getRandom().nextFloat() * (aabb.maxY - aabb.minY) + aabb.minY);
            float z = (float) (level.getRandom().nextFloat() * (aabb.maxZ - aabb.minZ) + aabb.minZ);
            level.addParticle(particleOptions, x, y, z, motionX, motionY, motionZ);
        }
    }

    public static void spawnParticles(Entity entity, ParticleOptions particleOptions, int count) {
        spawnParticles(entity.level(), particleOptions, count, entity.getBoundingBoxForCulling());
    }

    public static void spawnItemParticles(Entity entity, Item item, int count) {
        spawnItemParticles(entity.level(), item, count, entity.getBoundingBoxForCulling());
    }

    public static void spawnItemParticles(Level level, Item item, int count, AABB aabb) {
        spawnParticles(level, new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(item)), count, aabb);
    }

    public static boolean isEntityLargerThan(Entity entity, float size) {
        return entity.getBbWidth() > size;
    }

    public static boolean canBreak(Level level, BlockPos targetPos, float maxHardness) {
        BlockState state = level.getBlockState(targetPos);
        if (state.is(ModBlockTags.UNBREAKABLE) || state.getDestroySpeed(level, targetPos) >= maxHardness || state.getDestroySpeed(level, targetPos) < 0) {
            return false;
        }
        return !state.getCollisionShape(level, targetPos).isEmpty() && state.getFluidState().isEmpty();
    }

    public static boolean canReachPrey(Prehistoric prehistoric, Entity target) {
        if (!prehistoric.getSensing().hasLineOfSight(target)) {
            return false;
        }

        // Prefer the dinosaur's custom attack bounds, but fall back to ordinary
        // entity-body overlap. Some large multipart dinosaurs can visually make
        // contact while their custom attack box does not intersect the target.
        AABB targetBox = target.getBoundingBox();
        AABB attackBounds = prehistoric.getEntityHitboxData().getAttackBounds();
        if (attackBounds.intersects(targetBox)) {
            return true;
        }

        return prehistoric.getBoundingBox().inflate(1.0D, 0.5D, 1.0D).intersects(targetBox);
    }

    public static boolean canSeeFood(Prehistoric dino, BlockPos position) {
        Vec3 target = new Vec3(position.getX() + 0.5, position.getY(), position.getZ() + 0.5);
        ClipContext.Fluid fluid = ClipContext.Fluid.NONE;
        if (!(dino instanceof SwimmingAnimal)) {
            fluid = ClipContext.Fluid.ANY;
        }
        BlockHitResult rayTrace = dino.level().clip(new ClipContext(dino.getEyePosition(), target, ClipContext.Block.COLLIDER, fluid, dino));
        return position.equals(rayTrace.getBlockPos());
    }

    /**
     * Returns the average blocks per second for the given {@link net.minecraft.world.entity.ai.attributes.Attributes#MOVEMENT_SPEED MOVEMENT_SPEED} value.
     * Should be usable for all mobs using {@link com.github.teamfossilsarcheology.fossil.entity.ai.control.SmoothTurningMoveControl SmoothTurningMoveControl}
     * with speed values ranging from ~0.08 to ~0.25. Formula is based on sample data
     */
    public static double attributeToSpeed(double speed) {
        return 44.23174 * Mth.square(speed) - 0.504912 * speed + 0.0592455;
    }

    public static double attributeToSpeed(double speed, double sprintMod, boolean isSprinting) {
        return attributeToSpeed(isSprinting ? speed * sprintMod : speed);
    }

    public static double calculateSpeed(EntityDataLoader.Data data, float scale, boolean swim) {
        Attribute attributes = data.attributes();
        double baseSpeed = swim ? attributes.baseSwimSpeed() : attributes.baseSpeed();
        double minSpeed = swim ? attributes.minSwimSpeed() : attributes.minSpeed();
        double maxSpeed = swim ? attributes.maxSwimSpeed() : attributes.maxSpeed();
        double newSpeed = baseSpeed;
        boolean minAbove1 = data.minScale() >= 1;
        boolean maxBelow1 = data.maxScale() <= 1;
        if (scale < 1) {
            float min = data.minScale();
            float max = maxBelow1 ? data.maxScale() : 1;
            if (min != max) {
                newSpeed = Mth.lerp((scale - min) / (max - min), minSpeed, maxBelow1 ? maxSpeed : baseSpeed);
            }
        } else {
            float min = data.minScale() < 1 ? 1 : data.minScale();
            float max = data.maxScale();
            if (max != min) {
                newSpeed = Mth.lerp((scale - min) / (max - min), minAbove1 ? minSpeed : baseSpeed, maxSpeed);
            } else {
                newSpeed = maxSpeed;
            }
        }
        return newSpeed;
    }

    @Nullable
    public static <T extends Entity> T getNearestEntity(Class<? extends T> entityClazz, Mob attacker, AABB searchArea, Predicate<T> predicate) {
        List<? extends T> entities = attacker.level().getEntitiesOfClass(entityClazz, searchArea, entity -> true);
        double shortestDist = -1;
        T target = null;
        for (T entity : entities) {
            if (!attacker.getSensing().hasLineOfSight(entity) || !predicate.test(entity)) continue;
            double currentDist = entity.distanceToSqr(attacker);
            if (shortestDist != -1 && currentDist >= shortestDist) continue;
            shortestDist = currentDist;
            target = entity;
        }
        return target;
    }

    public static float yRotToYaw(double yRot) {
        return (float) Mth.wrapDegrees(yRot + 90);
    }

    public static float yawToYRot(double yaw) {
        return (float) Mth.wrapDegrees(yaw - 90);
    }

    public static float clampTo360(double x) {
        return (float) (x - Math.floor(x / 360) * 360);
    }

    public static Vec3 directionVecTo(Entity start, Entity end) {
        return end.position().subtract(start.position());
    }

    public static boolean movingAwayFrom(Entity target, Entity start) {
        return target.getDeltaMovement().length() > 0.2 && target.getDeltaMovement().dot(directionVecTo(start, target)) > 0;
    }

    public static Pair<Direction, Double> getClosestSide(AABB bounding, BlockPos blockPos) {
        AABB aabb = bounding.move(Vec3.atBottomCenterOf(blockPos).scale(-1));
        double maxX = Math.abs(Math.abs(aabb.maxX) - 0.5);
        double minZ = Math.abs(Math.abs(aabb.minZ) - 0.5);
        double maxZ = Math.abs(Math.abs(aabb.maxZ) - 0.5);
        double smallest = Math.abs(Math.abs(aabb.minX) - 0.5);
        Direction dir = Direction.WEST;
        if (maxX < smallest) {
            smallest = maxX;
            dir = Direction.EAST;
        }
        if (minZ < smallest) {
            smallest = minZ;
            dir = Direction.NORTH;
        }
        if (maxZ < smallest) {
            smallest = maxZ;
            dir = Direction.SOUTH;
        }
        return Pair.of(dir, smallest);
    }
}
