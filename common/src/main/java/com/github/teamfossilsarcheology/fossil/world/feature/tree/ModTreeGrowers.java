package com.github.teamfossilsarcheology.fossil.world.feature.tree;

import com.github.teamfossilsarcheology.fossil.world.feature.configuration.ModConfiguredFeatures;
import net.minecraft.world.level.block.grower.TreeGrower;

import java.util.Optional;

/**
 * 1.21: {@code AbstractTreeGrower} (subclassable) was replaced by the final {@link TreeGrower},
 * constructed with the {@code ConfiguredFeature} keys to grow. Each grower below carries a single
 * tree feature (the middle "tree" slot; mega/flower variants are empty).
 */
public final class ModTreeGrowers {
    public static final TreeGrower CALAMITES = single("calamites", ModConfiguredFeatures.CALAMITES_TREE_KEY);
    public static final TreeGrower CORDAITES = single("cordaites", ModConfiguredFeatures.CORDAITES_TREE_KEY);
    public static final TreeGrower MUTANT = single("mutant", ModConfiguredFeatures.MUTANT_TREE_KEY);
    public static final TreeGrower PALM = single("palm", ModConfiguredFeatures.PALM_TREE_KEY);
    public static final TreeGrower SIGILLARIA = single("sigillaria", ModConfiguredFeatures.SIGILLARIA_TREE_KEY);
    public static final TreeGrower TEMPSKYA = single("tempskya", ModConfiguredFeatures.TEMPSKYA_TREE_KEY);

    private static TreeGrower single(String name, net.minecraft.resources.ResourceKey<net.minecraft.world.level.levelgen.feature.ConfiguredFeature<?, ?>> tree) {
        return new TreeGrower(name, Optional.empty(), Optional.of(tree), Optional.empty());
    }

    private ModTreeGrowers() {
    }
}
