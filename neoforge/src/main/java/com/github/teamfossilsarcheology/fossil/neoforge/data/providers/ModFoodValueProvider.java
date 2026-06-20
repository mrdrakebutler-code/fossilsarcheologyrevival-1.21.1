package com.github.teamfossilsarcheology.fossil.neoforge.data.providers;

import com.github.teamfossilsarcheology.fossil.block.ModBlocks;
import com.github.teamfossilsarcheology.fossil.block.PrehistoricPlantInfo;
import com.github.teamfossilsarcheology.fossil.food.FoodType;
import com.github.teamfossilsarcheology.fossil.food.FoodValueProvider;
import com.github.teamfossilsarcheology.fossil.item.ModItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;


public class ModFoodValueProvider extends FoodValueProvider {

    public ModFoodValueProvider(PackOutput output) {
        super(output);
    }

    protected void buildFoodValues() {
        var egg = type(FoodType.EGG);
        var fish = type(FoodType.FISH);
        var meat = type(FoodType.MEAT);
        var plant = type(FoodType.PLANT);

        egg.item(Items.EGG, 7);

        plant.itemTag(TagKey.create(Registries.ITEM, ResourceLocation.parse("c:foods/berry")));
        plant.itemTag(TagKey.create(Registries.ITEM, ResourceLocation.parse("c:foods/bread")));
        plant.itemTag(TagKey.create(Registries.ITEM, ResourceLocation.parse("c:foods/fruit")));
        plant.itemTag(TagKey.create(Registries.ITEM, ResourceLocation.parse("c:foods/vegetable")));
        plant.blockTag(BlockTags.LEAVES, 20);
        plant.blockTag(BlockTags.FLOWERS, 5);
        plant.blockTag(BlockTags.SAPLINGS, 15);
        //crop: nutrition * max drop
        plant.block(Blocks.BEETROOTS, Foods.BEETROOT.nutrition() + 2);
        plant.block(Blocks.BROWN_MUSHROOM, 3);
        plant.block(Blocks.CAKE, 14);
        plant.block(Blocks.CARROTS, Foods.CARROT.nutrition() * 5);
        plant.block(Blocks.CHORUS_FLOWER, Foods.CHORUS_FRUIT.nutrition() * 2);
        plant.block(Blocks.CHORUS_PLANT, Foods.CHORUS_FRUIT.nutrition());
        plant.block(Blocks.SHORT_GRASS, 1);
        plant.block(Blocks.HAY_BLOCK, 15);
        plant.block(Blocks.KELP, Foods.DRIED_KELP.nutrition());
        plant.block(Blocks.KELP_PLANT, Foods.DRIED_KELP.nutrition());
        plant.block(Blocks.LILY_PAD, 2);
        plant.block(Blocks.MELON, Foods.MELON_SLICE.nutrition() * 5);
        plant.block(Blocks.POTATOES, Foods.POTATO.nutrition() * 5);
        plant.block(Blocks.PUMPKIN, Foods.MELON_SLICE.nutrition() * 4);
        plant.block(Blocks.RED_MUSHROOM, 3);
        plant.block(Blocks.SUGAR_CANE, 3);
        plant.block(Blocks.SWEET_BERRY_BUSH, Foods.SWEET_BERRIES.nutrition() * 3);
        plant.block(Blocks.TALL_GRASS, 2);
        plant.block(Blocks.WHEAT, 3);

        plant.block(PrehistoricPlantInfo.BENNETTITALES_LARGE.getPlantBlock(), 6);
        plant.block(PrehistoricPlantInfo.BENNETTITALES_SMALL.getPlantBlock(), 3);
        plant.block(PrehistoricPlantInfo.CEPHALOTAXUS.getPlantBlock(), 3);
        plant.block(PrehistoricPlantInfo.CRATAEGUS.getPlantBlock(), Foods.SWEET_BERRIES.nutrition() * 3 + 1);
        plant.block(PrehistoricPlantInfo.CYATHEA.getPlantBlock(), 12);
        plant.block(PrehistoricPlantInfo.DICTYOPHYLLUM.getPlantBlock(), 3);
        plant.block(PrehistoricPlantInfo.DILLHOFFIA.getPlantBlock(), 3);
        plant.block(PrehistoricPlantInfo.DIPTERIS.getPlantBlock(), 6);
        plant.block(PrehistoricPlantInfo.DUISBERGIA.getPlantBlock(), 6);
        plant.block(PrehistoricPlantInfo.EPHEDRA.getPlantBlock(), Foods.SWEET_BERRIES.nutrition() * 2 + 1);
        plant.block(PrehistoricPlantInfo.FLORISSANTIA.getPlantBlock(), 3);
        plant.block(PrehistoricPlantInfo.FOOZIA.getPlantBlock(), 6);
        plant.block(PrehistoricPlantInfo.HORSETAIL_LARGE.getPlantBlock(), 6);
        plant.block(PrehistoricPlantInfo.HORSETAIL_SMALL.getPlantBlock(), 3);
        plant.block(PrehistoricPlantInfo.LICOPODIOPHYTA.getPlantBlock(), 3);
        plant.block(PrehistoricPlantInfo.OSMUNDA.getPlantBlock(), 3);
        plant.block(PrehistoricPlantInfo.SAGENOPTERIS.getPlantBlock(), 3);
        plant.block(PrehistoricPlantInfo.SARRACENIA.getPlantBlock(), 6);
        plant.block(PrehistoricPlantInfo.VACCINIUM.getPlantBlock(), Foods.SWEET_BERRIES.nutrition() * 3 + 1);
        plant.block(PrehistoricPlantInfo.WELWITSCHIA.getPlantBlock(), 3);
        plant.block(PrehistoricPlantInfo.ZAMITES.getPlantBlock(), 6);
        plant.block(ModBlocks.FERNS.get(), 3);
        plant.item(ModItems.FERN_SEED.get(), 5);

        plant.item(Items.APPLE);
        plant.item(Items.BAKED_POTATO);
        plant.item(Items.BEETROOT);
        plant.item(Items.BEETROOT_SEEDS, 5);
        plant.item(Items.BREAD);
        plant.item(Items.CAKE, 14 * 5);
        plant.item(Items.CARROT);
        plant.item(Items.CHORUS_FRUIT);
        plant.item(Items.COOKIE);
        plant.item(Items.DRIED_KELP);
        plant.item(Items.GLOW_BERRIES);
        plant.item(Items.MELON_SEEDS, 5);
        plant.item(Items.MELON_SLICE);
        plant.item(Items.POTATO);
        plant.item(Items.PUMPKIN_PIE);
        plant.item(Items.PUMPKIN_SEEDS, 5);
        plant.item(Items.SUGAR, 7);
        plant.item(Items.SUGAR_CANE, 15);
        plant.item(Items.SWEET_BERRIES);
        plant.item(Items.WHEAT, 13);
        plant.item(Items.WHEAT_SEEDS, 5);

        fish.itemTag(TagKey.create(Registries.ITEM, ResourceLocation.parse("c:foods/raw_fish")));
        fish.itemTag(TagKey.create(Registries.ITEM, ResourceLocation.parse("c:foods/cooked_fish")));
        fish.item(Items.COD);
        fish.item(Items.PUFFERFISH);
        fish.item(Items.SALMON);
        fish.item(Items.TROPICAL_FISH);
        fish.item(Items.COOKED_COD);
        fish.item(Items.COOKED_SALMON);

        meat.itemTag(TagKey.create(Registries.ITEM, ResourceLocation.parse("c:foods/raw_meat")));
        meat.itemTag(TagKey.create(Registries.ITEM, ResourceLocation.parse("c:foods/cooked_meat")));
        meat.item(Items.PORKCHOP);
        meat.item(Items.COOKED_PORKCHOP);
        meat.item(Items.BEEF);
        meat.item(Items.COOKED_BEEF);
        meat.item(Items.COOKED_CHICKEN);
        meat.item(Items.CHICKEN);
        meat.item(ModItems.FAILURESAURUS_FLESH.get(), 15);
        meat.item(Items.MUTTON);
        meat.item(Items.COOKED_MUTTON);
        meat.item(Items.RABBIT);
        meat.item(Items.COOKED_RABBIT);
        meat.item(Items.RABBIT_FOOT, 7);

        meat.entity(EntityType.AXOLOTL, 5);
        meat.entity(EntityType.BAT, 5);
        meat.entity(EntityType.CAT, 10);
        meat.entity(EntityType.CHICKEN, 7);//Drops not included
        fish.entity(EntityType.COD, 5);
        meat.entity(EntityType.COW, 40);
        fish.entity(EntityType.DOLPHIN, 17);
        meat.entity(EntityType.DONKEY, 45);
        meat.entity(EntityType.FOX, 15);
        meat.entity(EntityType.GOAT, 30);
        fish.entity(EntityType.GLOW_SQUID, 20);
        meat.entity(EntityType.HOGLIN, 55);
        meat.entity(EntityType.HORSE, 55);
        meat.entity(EntityType.LLAMA, 50);
        meat.entity(EntityType.MOOSHROOM, 40);
        meat.entity(EntityType.MULE, 50);
        meat.entity(EntityType.OCELOT, 4);
        meat.entity(EntityType.PANDA, 27);
        meat.entity(EntityType.PARROT, 2);
        meat.entity(EntityType.PIG, 20);
        meat.entity(EntityType.POLAR_BEAR, 60);
        fish.entity(EntityType.PUFFERFISH, 5);
        meat.entity(EntityType.RABBIT, 5);
        fish.entity(EntityType.SALMON, 5);
        meat.entity(EntityType.SHEEP, 35);
        fish.entity(EntityType.SQUID, 20);
        fish.entity(EntityType.TROPICAL_FISH, 5);
        fish.entity(EntityType.TURTLE, 5);
        meat.entity(EntityType.WOLF, 15);

        meat.entity(EntityType.PLAYER, 27);
        meat.entity(EntityType.VILLAGER, 27);
        fish.entity(EntityType.GUARDIAN, 65);
        meat.entity(EntityType.SPIDER, 30);
        meat.entity(EntityType.CAVE_SPIDER, 15);


        meat.entity(ResourceLocation.fromNamespaceAndPath("rats", "rat"), 5);

        meat.entity(ResourceLocation.fromNamespaceAndPath("bewitchment", "owl"), 7);
        meat.entity(ResourceLocation.fromNamespaceAndPath("bewitchment", "raven"), 5);
        meat.entity(ResourceLocation.fromNamespaceAndPath("bewitchment", "snake"), 4);
        meat.entity(ResourceLocation.fromNamespaceAndPath("bewitchment", "toad"), 3);

        //TODO: Farmers delight tags?
        String betterAnimalsPlus = "betteranimalsplus";
        meat.entity(ResourceLocation.fromNamespaceAndPath(betterAnimalsPlus, "deer"), 35);
        meat.entity(ResourceLocation.fromNamespaceAndPath(betterAnimalsPlus, "pheasant"), 10);
        meat.entity(ResourceLocation.fromNamespaceAndPath(betterAnimalsPlus, "turkey"), 10);
        meat.entity(ResourceLocation.fromNamespaceAndPath(betterAnimalsPlus, "goose"), 10);
        meat.entity(ResourceLocation.fromNamespaceAndPath(betterAnimalsPlus, "boar"), 30);
        meat.entity(ResourceLocation.fromNamespaceAndPath(betterAnimalsPlus, "moose"), 45);
        meat.entity(ResourceLocation.fromNamespaceAndPath(betterAnimalsPlus, "reindeer"), 35);
        meat.entity(ResourceLocation.fromNamespaceAndPath(betterAnimalsPlus, "squirrel"), 3);
        meat.entity(ResourceLocation.fromNamespaceAndPath(betterAnimalsPlus, "songbird"), 3);
        meat.entity(ResourceLocation.fromNamespaceAndPath(betterAnimalsPlus, "lammergeier"), 8);
        meat.entity(ResourceLocation.fromNamespaceAndPath(betterAnimalsPlus, "gazelle"), 15);
        fish.entity(ResourceLocation.fromNamespaceAndPath(betterAnimalsPlus, "horseshoecrab"), 7);
        fish.entity(ResourceLocation.fromNamespaceAndPath(betterAnimalsPlus, "nautilus"), 10);
        fish.entity(ResourceLocation.fromNamespaceAndPath(betterAnimalsPlus, "lamprey"), 5);
        fish.entity(ResourceLocation.fromNamespaceAndPath(betterAnimalsPlus, "crab"), 5);
        fish.entity(ResourceLocation.fromNamespaceAndPath(betterAnimalsPlus, "shark"), 40);
        fish.entity(ResourceLocation.fromNamespaceAndPath(betterAnimalsPlus, "eel"), 20);
        fish.entity(ResourceLocation.fromNamespaceAndPath(betterAnimalsPlus, "whale"), 60);
        fish.entity(ResourceLocation.fromNamespaceAndPath(betterAnimalsPlus, "flying_fish"), 5);

        meat.entity(ResourceLocation.fromNamespaceAndPath("totemic", "buffalo"), 55);
        meat.entity(ResourceLocation.fromNamespaceAndPath("totemic", "bald_eagle"), 8);

        meat.entity(ResourceLocation.fromNamespaceAndPath("quark", "crab"), 5);
        meat.entity(ResourceLocation.fromNamespaceAndPath("quark", "frog"), 3);

        String exoticBirds = "exoticbirds";
        meat.entity(ResourceLocation.fromNamespaceAndPath(exoticBirds, "cassowary"), 25);
        meat.entity(ResourceLocation.fromNamespaceAndPath(exoticBirds, "duck"), 10);
        meat.entity(ResourceLocation.fromNamespaceAndPath(exoticBirds, "flamingo"), 7);
        meat.entity(ResourceLocation.fromNamespaceAndPath(exoticBirds, "gouldianfinch"), 3);
        meat.entity(ResourceLocation.fromNamespaceAndPath(exoticBirds, "hummingbird"), 3);
        meat.entity(ResourceLocation.fromNamespaceAndPath(exoticBirds, "kingfisher"), 5);
        meat.entity(ResourceLocation.fromNamespaceAndPath(exoticBirds, "kiwi"), 3);
        meat.entity(ResourceLocation.fromNamespaceAndPath(exoticBirds, "lyrebird"), 5);
        meat.entity(ResourceLocation.fromNamespaceAndPath(exoticBirds, "magpie"), 3);
        meat.entity(ResourceLocation.fromNamespaceAndPath(exoticBirds, "ostrich"), 27);
        meat.entity(ResourceLocation.fromNamespaceAndPath(exoticBirds, "owl"), 7);
        meat.entity(ResourceLocation.fromNamespaceAndPath(exoticBirds, "parrot"), 5);
        meat.entity(ResourceLocation.fromNamespaceAndPath(exoticBirds, "peafowl"), 10);
        meat.entity(ResourceLocation.fromNamespaceAndPath(exoticBirds, "pelican"), 5);
        meat.entity(ResourceLocation.fromNamespaceAndPath(exoticBirds, "emperorpenguin"), 7);
        meat.entity(ResourceLocation.fromNamespaceAndPath(exoticBirds, "pigeon"), 3);
        meat.entity(ResourceLocation.fromNamespaceAndPath(exoticBirds, "roadrunner"), 5);
        meat.entity(ResourceLocation.fromNamespaceAndPath(exoticBirds, "seagull"), 3);
        meat.entity(ResourceLocation.fromNamespaceAndPath(exoticBirds, "swan"), 12);
        meat.entity(ResourceLocation.fromNamespaceAndPath(exoticBirds, "toucan"), 7);
        meat.entity(ResourceLocation.fromNamespaceAndPath(exoticBirds, "vulture"), 7);
        meat.entity(ResourceLocation.fromNamespaceAndPath(exoticBirds, "woodpecker"), 3);
        meat.entity(ResourceLocation.fromNamespaceAndPath(exoticBirds, "heron"), 15);
        meat.entity(ResourceLocation.fromNamespaceAndPath(exoticBirds, "booby"), 7);
        meat.entity(ResourceLocation.fromNamespaceAndPath(exoticBirds, "cardinal"), 3);
        meat.entity(ResourceLocation.fromNamespaceAndPath(exoticBirds, "bluejay"), 3);
        meat.entity(ResourceLocation.fromNamespaceAndPath(exoticBirds, "robin"), 3);
        meat.entity(ResourceLocation.fromNamespaceAndPath(exoticBirds, "crane"), 15);
        meat.entity(ResourceLocation.fromNamespaceAndPath(exoticBirds, "kookaburra"), 5);
        meat.entity(ResourceLocation.fromNamespaceAndPath(exoticBirds, "budgerigar"), 3);

        meat.entity(ResourceLocation.fromNamespaceAndPath("twilightforest", "boar"), 20);
        meat.entity(ResourceLocation.fromNamespaceAndPath("twilightforest", "bighorn_sheep"), 35);
        meat.entity(ResourceLocation.fromNamespaceAndPath("twilightforest", "deer"), 35);
        meat.entity(ResourceLocation.fromNamespaceAndPath("twilightforest", "penguin"), 10);
        meat.entity(ResourceLocation.fromNamespaceAndPath("twilightforest", "squirrel"), 3);

        meat.entity(ResourceLocation.fromNamespaceAndPath("naturalist", "boar"), 10);
        meat.entity(ResourceLocation.fromNamespaceAndPath("naturalist", "bear"), 35);
        meat.entity(ResourceLocation.fromNamespaceAndPath("naturalist", "deer"), 40);
        meat.entity(ResourceLocation.fromNamespaceAndPath("naturalist", "snake"), 10);
        meat.entity(ResourceLocation.fromNamespaceAndPath("naturalist", "coral_snake"), 10);
        meat.entity(ResourceLocation.fromNamespaceAndPath("naturalist", "rattlesnake"), 10);
        meat.entity(ResourceLocation.fromNamespaceAndPath("naturalist", "rhino"), 60);
        meat.entity(ResourceLocation.fromNamespaceAndPath("naturalist", "zebra"), 40);
        meat.entity(ResourceLocation.fromNamespaceAndPath("naturalist", "giraffe"), 50);
        meat.entity(ResourceLocation.fromNamespaceAndPath("naturalist", "vulture"), 15);
        meat.entity(ResourceLocation.fromNamespaceAndPath("naturalist", "ostrich"), 35);
        fish.entity(ResourceLocation.fromNamespaceAndPath("naturalist", "catfish"), 10);
        fish.entity(ResourceLocation.fromNamespaceAndPath("naturalist", "bass"), 10);
        fish.entity(ResourceLocation.fromNamespaceAndPath("naturalist", "duck"), 10);
    }

    @Override
    public @NotNull String getName() {
        return "Fossil Food Values";
    }
}
