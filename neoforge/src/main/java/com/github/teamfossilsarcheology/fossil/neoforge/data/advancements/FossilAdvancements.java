package com.github.teamfossilsarcheology.fossil.neoforge.data.advancements;

import com.github.teamfossilsarcheology.fossil.FossilMod;
import com.github.teamfossilsarcheology.fossil.advancements.ImplantEmbryoTrigger;
import com.github.teamfossilsarcheology.fossil.advancements.IncubateEggTrigger;
import com.github.teamfossilsarcheology.fossil.advancements.ModTriggers;
import com.github.teamfossilsarcheology.fossil.advancements.OpenSarcophagusTrigger;
import com.github.teamfossilsarcheology.fossil.advancements.ScarabTameTrigger;
import com.github.teamfossilsarcheology.fossil.entity.ModEntities;
import com.github.teamfossilsarcheology.fossil.entity.prehistoric.base.PrehistoricEntityInfo;
import com.github.teamfossilsarcheology.fossil.entity.prehistoric.base.VanillaEntityInfo;
import com.github.teamfossilsarcheology.fossil.tags.ModItemTags;
import com.github.teamfossilsarcheology.fossil.world.dimension.ModDimensions;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.advancements.*;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.github.teamfossilsarcheology.fossil.block.ModBlocks.*;
import static com.github.teamfossilsarcheology.fossil.entity.prehistoric.base.PrehistoricEntityInfo.*;
import static com.github.teamfossilsarcheology.fossil.item.ModItems.*;

public class FossilAdvancements implements Consumer<Consumer<AdvancementHolder>> {
    @Override
    public void accept(Consumer<AdvancementHolder> consumer) {
        AdvancementHolder root = Advancement.Builder.advancement().display(BIO_FOSSIL.get(),
                        Component.translatable("advancements.fossil.root.title"),
                        Component.translatable("advancements.fossil.root.description"),
                        FossilMod.location("textures/block/ancient_stone_bricks.png"), AdvancementType.TASK, true, false, false)
                .addCriterion("requirement", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.CRAFTING_TABLE))
                .save(consumer, FossilMod.MOD_ID + ":fossil/root");
        AdvancementHolder breakFossil = simple(root, "break_fossil", consumer, RELIC_SCRAP, BIO_FOSSIL, PlANT_FOSSIL, SHALE_FOSSIL, SKULL_BLOCK);
        AdvancementHolder analyzer = simple(breakFossil, consumer, ANALYZER);
        AdvancementHolder fossilSeed = tag(FERN_SEED_FOSSIL.get(), ModItemTags.FOSSIL_SEEDS, analyzer, consumer);
        AdvancementHolder restoredSeed = tag(FERN_SEED.get(), ModItemTags.RESTORED_SEEDS, fossilSeed, consumer);

        AdvancementHolder figurine = tag(STEVE_FIGURINE_DESTROYED.get(), ModItemTags.FIGURINES, breakFossil, consumer);
        AdvancementHolder anuLair = other(figurine, ANU_STATUE, "anu_lair", ChangeDimensionTrigger.TriggerInstance.changedDimensionTo(ModDimensions.ANU_LAIR), consumer);
        AdvancementHolder anubite = other(anuLair, ANUBITE_STATUE, "anubite",
                KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(ModEntities.ANUBITE.get())), consumer);
        AdvancementHolder sarcophagus = other(anuLair, SARCOPHAGUS, "sarcophagus",
                new Criterion<>(ModTriggers.OPEN_SARCOPHAGUS_TRIGGER, OpenSarcophagusTrigger.TriggerInstance.useScarab()), consumer);
        AdvancementHolder killAnu = other(sarcophagus, ANCIENT_KEY, "kill_anu",
                KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(ModEntities.ANU_BOSS.get())), consumer);
        AdvancementHolder ancientClock = simple(killAnu, consumer, ANCIENT_CLOCK);

        AdvancementHolder frozenMeat = simple(breakFossil, consumer, FROZEN_MEAT);
        AdvancementHolder scarabGem = simple(breakFossil, consumer, SCARAB_GEM);
        AdvancementHolder scarabTame = other(scarabGem, AQUATIC_SCARAB_GEM, "scarab_tame",
                new Criterion<>(ModTriggers.SCARAB_TAME_TRIGGER, ScarabTameTrigger.TriggerInstance.scarabTame()), consumer);

        AdvancementHolder stoneTablet = simple(analyzer, consumer, STONE_TABLET);
        AdvancementHolder tarDrop = simple(breakFossil, consumer, TAR_DROP);
        AdvancementHolder tarFossil = simple(tarDrop, consumer, TAR_FOSSIL);

        AdvancementHolder brokenSword = simple(root, consumer, BROKEN_SWORD, BROKEN_HELMET);
        AdvancementHolder worktable = simple(brokenSword, consumer, WORKTABLE);
        AdvancementHolder ancientSword = simple(worktable, consumer, ANCIENT_SWORD, ANCIENT_HELMET);

        AdvancementHolder dna = tag(TRICERATOPS.dnaItem, ModItemTags.DNA, analyzer, consumer);
        AdvancementHolder cultureVat = simple(dna, consumer, CULTURE_VAT);
        AdvancementHolder failuresaurus = other(cultureVat, FAILURESAURUS_FLESH, "failuresaurus",
                PlayerHurtEntityTrigger.TriggerInstance.playerHurtEntity(Optional.of(EntityPredicate.Builder.entity().of(ModEntities.FAILURESAURUS.get()).build())), consumer);
        AdvancementHolder embryo = tag(MAMMOTH.embryoItem, ModItemTags.EMBRYOS, cultureVat, consumer);
        AdvancementHolder dinoEgg = tag(TRICERATOPS.eggItem, ModItemTags.ALL_EGGS, cultureVat, consumer, "dino_eggs");
        AdvancementHolder dinopedia = simple(dinoEgg, consumer, DINOPEDIA);

        Advancement.Builder builder = Advancement.Builder.advancement().display(TYRANNOSAURUS.eggItem, title("all_eggs"),
                        description("all_eggs"), null, AdvancementType.CHALLENGE, true, true, false)
                .parent(dinoEgg).rewards(AdvancementRewards.Builder.experience(1000));
        for (PrehistoricEntityInfo info : PrehistoricEntityInfo.values()) {
            if (info.eggItem != null || info.cultivatedBirdEggItem != null) {
                builder.addCriterion(key(info.entityType()).getPath(), new Criterion<>(ModTriggers.INCUBATE_EGG_TRIGGER, IncubateEggTrigger.TriggerInstance.incubateEgg(info.entityType())));
            }
        }
        builder.save(consumer, FossilMod.MOD_ID + ":fossil/all_eggs");

        builder = Advancement.Builder.advancement().display(MAMMOTH.embryoItem, title("all_embryos"), description("all_embryos"),
                        null, AdvancementType.CHALLENGE, true, true, false)
                .parent(embryo).rewards(AdvancementRewards.Builder.experience(500));
        for (PrehistoricEntityInfo info : values()) {
            if (info.embryoItem != null) {
                builder.addCriterion(key(info.embryoItem).getPath(), new Criterion<>(ModTriggers.IMPLANT_EMBRYO_TRIGGER, ImplantEmbryoTrigger.TriggerInstance.implantEmbryo(info.embryoItem)));
            }
        }
        for (VanillaEntityInfo info : VanillaEntityInfo.values()) {
            if (info.embryoItem != null) {
                builder.addCriterion(key(info.embryoItem).getPath(), new Criterion<>(ModTriggers.IMPLANT_EMBRYO_TRIGGER, ImplantEmbryoTrigger.TriggerInstance.implantEmbryo(info.embryoItem)));
            }
        }
        builder.save(consumer, FossilMod.MOD_ID + ":fossil/all_embryos");

    }

    private AdvancementHolder other(AdvancementHolder parent, RegistrySupplier<? extends ItemLike> item, String key, Criterion<?> trigger, Consumer<AdvancementHolder> consumer) {
        return Advancement.Builder.advancement().display(item.get(), title(key), description(key),
                        null, AdvancementType.TASK, true, true, false)
                .parent(parent)
                .addCriterion("requirement", trigger)
                .save(consumer, FossilMod.MOD_ID + ":fossil/" + key);
    }

    private AdvancementHolder simple(AdvancementHolder parent, String key, Consumer<AdvancementHolder> consumer, ItemLike... items) {
        ItemLike item = items[0];
        Advancement.Builder builder = Advancement.Builder.advancement().display(item, title(key), description(key),
                        null, AdvancementType.TASK, true, true, false)
                .parent(parent).requirements(AdvancementRequirements.Strategy.OR);
        for (ItemLike itemLike : items) {
            builder.addCriterion(key(itemLike.asItem()).getPath(), InventoryChangeTrigger.TriggerInstance.hasItems(itemLike));
        }
        return builder.save(consumer, FossilMod.MOD_ID + ":fossil/" + key);
    }

    @SafeVarargs
    private AdvancementHolder simple(AdvancementHolder parent, String key, Consumer<AdvancementHolder> consumer, RegistrySupplier<? extends ItemLike>... items) {
        return simple(parent, key, consumer, Arrays.stream(items).map(Supplier::get).toArray(ItemLike[]::new));
    }

    @SafeVarargs
    private AdvancementHolder simple(AdvancementHolder parent, Consumer<AdvancementHolder> consumer, RegistrySupplier<? extends ItemLike>... items) {
        return simple(parent, key(items[0].get().asItem()).getPath(), consumer, Arrays.stream(items).map(Supplier::get).toArray(ItemLike[]::new));
    }

    private AdvancementHolder tag(ItemLike item, TagKey<Item> tag, AdvancementHolder parent, Consumer<AdvancementHolder> consumer, String key) {
        return Advancement.Builder.advancement().display(item, title(key), description(key),
                        null, AdvancementType.TASK, true, true, false)
                .parent(parent)
                .addCriterion("requirement", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(tag).build()))
                .save(consumer, FossilMod.MOD_ID + ":fossil/" + key);
    }

    private AdvancementHolder tag(ItemLike item, TagKey<Item> tag, AdvancementHolder parent, Consumer<AdvancementHolder> consumer) {
        String key = tag.location().getPath();
        return tag(item, tag, parent, consumer, key);
    }

    private Component title(String key) {
        return Component.translatable(String.format("advancements.%s.%s.title", FossilMod.MOD_ID, key));
    }

    private Component description(String key) {
        return Component.translatable(String.format("advancements.%s.%s.description", FossilMod.MOD_ID, key));
    }

    private ResourceLocation key(Item item) {
        return BuiltInRegistries.ITEM.getKey(item);
    }

    private ResourceLocation key(EntityType<?> entityType) {
        return BuiltInRegistries.ENTITY_TYPE.getKey(entityType);
    }
}
