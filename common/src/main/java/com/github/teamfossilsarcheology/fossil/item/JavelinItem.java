package com.github.teamfossilsarcheology.fossil.item;

import com.github.teamfossilsarcheology.fossil.entity.Javelin;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class JavelinItem extends TieredItem {
    private final boolean ancient;

    public JavelinItem(Tier tier, boolean ancient) {
        super(tier, new JavelinProperties().stacksToWithDurability(16, 30).arch$tab(ModTabs.FA_OTHER_ITEM_TAB));
        this.ancient = ancient;
    }

    public JavelinItem(Tier tier) {
        this(tier, false);
    }

    /**
     * 1.21: vanilla enchantments are referenced by {@link ResourceKey} and {@link EnchantmentHelper}
     * works on {@code Holder<Enchantment>}. Resolve the holder from the level's registry access.
     */
    private static Holder<Enchantment> enchantment(Level level, ResourceKey<Enchantment> key) {
        return level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(key);
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity shooter, int timeCharged) {
        if (!(shooter instanceof Player player)) {
            return;
        }
        boolean infiniteAmmo = player.getAbilities().instabuild || EnchantmentHelper.getItemEnchantmentLevel(enchantment(level, Enchantments.INFINITY), stack) > 0;
        int i = this.getUseDuration(stack, shooter) - timeCharged;
        float speed = BowItem.getPowerForTime(i);
        if (speed < 0.1) {
            return;
        }
        if (!level.isClientSide) {
            int damage = stack.getMaxDamage() - (stack.getDamageValue() + (player.getAbilities().instabuild ? 0 : 1));
            Javelin javelin = new Javelin(level, shooter, getTier(), ancient, damage);
            javelin.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, speed * 3, 1);
            if (speed == 1) {
                javelin.setCritArrow(true);
            }
            int powerLevel = EnchantmentHelper.getItemEnchantmentLevel(enchantment(level, Enchantments.POWER), stack);
            if (powerLevel > 0) {
                javelin.setBaseDamage(javelin.getBaseDamage() + powerLevel * 0.5 + 0.5);
            }
            int punchLevel = EnchantmentHelper.getItemEnchantmentLevel(enchantment(level, Enchantments.PUNCH), stack);
            if (punchLevel > 0) {
                // TODO(Phase 4): AbstractArrow.setKnockback(int) was removed in 1.21; arrow knockback is now
                //  driven by the punch enchantment's data-driven effects on the projectile. Re-wire once the
                //  Javelin entity exposes a knockback field again.
            }
            int flameLevel = EnchantmentHelper.getItemEnchantmentLevel(enchantment(level, Enchantments.FLAME), stack);
            if (flameLevel > 0) {
                javelin.igniteForSeconds(100);
            }
            if (infiniteAmmo) {
                javelin.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
            }
            level.addFreshEntity(javelin);
        }
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1,
                1 / (level.random.nextFloat() * 0.4f + 1.2f) + speed * 0.5f);
        if (!infiniteAmmo) {
            stack.shrink(1);
        }
        player.awardStat(Stats.ITEM_USED.get(this));
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 72000;
    }

    @Override
    public @NotNull UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack itemStack = player.getItemInHand(usedHand);
        player.startUsingItem(usedHand);
        return InteractionResultHolder.consume(itemStack);
    }

    @Override
    public int getEnchantmentValue() {
        return 0;
    }

    public static class JavelinProperties extends Item.Properties {

        public JavelinProperties stacksToWithDurability(int maxStackSize, int durability) {
            // 1.21: an item carrying the MAX_DAMAGE (durability) component is forced to a max stack size of 1
            //  (Properties.durability() sets MAX_STACK_SIZE=1, and buildAndValidateComponents rejects a damageable
            //  stackable item). The old behaviour (stack of 16 + per-item durability) is no longer expressible.
            // TODO(Phase 4): revisit javelin stacking — either drop durability or model "ammo count" some other way.
            durability(durability);
            return this;
        }
    }
}
