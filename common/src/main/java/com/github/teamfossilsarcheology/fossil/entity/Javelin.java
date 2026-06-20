package com.github.teamfossilsarcheology.fossil.entity;

import com.github.teamfossilsarcheology.fossil.item.ModItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class Javelin extends AbstractArrow {
    private static final EntityDataAccessor<Integer> TIER_ID = SynchedEntityData.defineId(Javelin.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> ANCIENT = SynchedEntityData.defineId(Javelin.class, EntityDataSerializers.BOOLEAN);
    private int itemDamage;

    public Javelin(EntityType<Javelin> type, Level level) {
        super(type, level);
    }

    public Javelin(Level level, LivingEntity shooter, Tier tier, boolean ancient, int itemDamage) {
        //1.21.1: AbstractArrow ctor gained (ItemStack pickupItemStack, ItemStack firedFromWeapon). Pickup stack is
        //derived from the ctor params (instance fields aren't set yet). firedFromWeapon must be null (NOT
        //ItemStack.EMPTY) — the ctor throws IllegalArgumentException("Invalid weapon firing an arrow") on an empty-but-
        //non-null weapon. The javelin isn't "fired from" a held weapon in the vanilla sense, so null = no weapon.
        super(ModEntities.JAVELIN.get(), shooter, level, createPickupItem(tier, ancient, itemDamage), null);
        this.itemDamage = itemDamage;
        if (tier instanceof Tiers tiers) {
            setTier(tiers);
        }
        entityData.set(ANCIENT, ancient);
        //TODO(Phase 5): setPierceLevel(byte) was removed from AbstractArrow (pierce now derives from the weapon's
        //Piercing enchantment). The javelin no longer pierces 16 entities; re-add via entity data or a weapon item if needed.
        setBaseDamage(getDamage(tier, ancient));
    }

    private static double getDamage(Tier tier, boolean ancient) {
        if (ancient) {
            return 5;
        } else if (tier instanceof Tiers tiers) {
            switch (tiers) {
                case WOOD -> {
                    return 2;
                }
                case STONE -> {
                    return 2.5;
                }
                case GOLD -> {
                    return 3.5;
                }
                case IRON -> {
                    return 3;
                }
                case DIAMOND -> {
                    return 4;
                }
            }
        }
        return 2;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ANCIENT, false);
        builder.define(TIER_ID, 0);
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        //1.21.1: setPierceLevel removed (see ctor TODO).
        if (level() instanceof ServerLevel && isAncient() && random.nextInt(100) < 30) {
            LightningBolt lightningBolt = EntityType.LIGHTNING_BOLT.create(level());
            lightningBolt.moveTo(Vec3.atBottomCenterOf(blockPosition()));
            lightningBolt.setCause(getOwner() instanceof ServerPlayer ? (ServerPlayer) getOwner() : null);
            level().addFreshEntity(lightningBolt);
        }
    }

    @Override
    protected void tickDespawn() {

    }

    public Tier getTier() {
        if (!isAncient()) {
            return Tiers.values()[entityData.get(TIER_ID)];
        }
        return Tiers.WOOD;
    }

    public void setTier(Tiers tier) {
        entityData.set(TIER_ID, tier.ordinal());
    }

    public boolean isAncient() {
        return entityData.get(ANCIENT);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("ancient", isAncient());
        if (getTier() instanceof Tiers tiers) {
            compound.putInt("Tier", tiers.ordinal());
        }
        compound.putInt("Damage", itemDamage);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        entityData.set(ANCIENT, compound.getBoolean("ancient"));
        if (!isAncient()) {
            setTier(Tiers.values()[compound.getInt("Tier")]);
        }
        itemDamage = compound.getInt("Damage");
    }

    @Override
    protected @NotNull ItemStack getDefaultPickupItem() {
        return createPickupItem(getTier(), isAncient(), itemDamage);
    }

    private static @NotNull ItemStack createPickupItem(Tier tier, boolean ancient, int itemDamage) {
        if (ancient) {
            ItemStack stack = new ItemStack(ModItems.ANCIENT_JAVELIN.get());
            stack.setDamageValue(stack.getMaxDamage() - itemDamage);
            return stack;
        } else if (tier instanceof Tiers tiers) {
            switch (tiers) {
                case WOOD -> {
                    ItemStack stack = new ItemStack(ModItems.WOODEN_JAVELIN.get());
                    stack.setDamageValue(stack.getMaxDamage() - itemDamage);
                    return stack;
                }
                case STONE -> {
                    ItemStack stack = new ItemStack(ModItems.STONE_JAVELIN.get());
                    stack.setDamageValue(stack.getMaxDamage() - itemDamage);
                    return stack;
                }
                case GOLD -> {
                    ItemStack stack = new ItemStack(ModItems.GOLD_JAVELIN.get());
                    stack.setDamageValue(stack.getMaxDamage() - itemDamage);
                    return stack;
                }
                case IRON -> {
                    ItemStack stack = new ItemStack(ModItems.IRON_JAVELIN.get());
                    stack.setDamageValue(stack.getMaxDamage() - itemDamage);
                    return stack;
                }
                case DIAMOND -> {
                    ItemStack stack = new ItemStack(ModItems.DIAMOND_JAVELIN.get());
                    stack.setDamageValue(stack.getMaxDamage() - itemDamage);
                    return stack;
                }
            }
        }
        return ItemStack.EMPTY;
    }
}
