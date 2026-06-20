package com.github.teamfossilsarcheology.fossil.capabilities.neoforge;

import com.github.teamfossilsarcheology.fossil.block.entity.CommonEnergyStorage;
import com.github.teamfossilsarcheology.fossil.config.FossilConfig;
import com.github.teamfossilsarcheology.fossil.entity.prehistoric.base.EntityInfo;
import com.github.teamfossilsarcheology.fossil.neoforge.capabilities.ModAttachments;
import com.github.teamfossilsarcheology.fossil.neoforge.capabilities.mammal.IMammalCap;
import com.github.teamfossilsarcheology.fossil.neoforge.capabilities.player.IFirstHatchCap;
import com.github.teamfossilsarcheology.fossil.neoforge.energy.FAEnergyStorage;
import com.github.teamfossilsarcheology.fossil.network.MessageHandler;
import com.github.teamfossilsarcheology.fossil.network.S2CMammalCapMessage;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

/**
 * 1.21 NeoForge: the former mammal/firstHatch capabilities are now data attachments (see
 * {@link ModAttachments}). {@code getData(...)} lazily creates and attaches the default value, so we no
 * longer cache {@code LazyOptional}s or attach via {@code AttachCapabilitiesEvent}.
 */
public class ModCapabilitiesImpl {

    public static IMammalCap getMammalCap(Animal animal) {
        return animal.getData(ModAttachments.MAMMAL);
    }

    public static IFirstHatchCap getFirstHatchCap(Player player) {
        return player.getData(ModAttachments.FIRST_HATCH);
    }

    public static boolean hasEmbryo(Animal animal) {
        return getMammalCap(animal).getEmbryo() != null;
    }

    public static int getEmbryoProgress(Animal animal) {
        return getMammalCap(animal).getEmbryoProgress();
    }

    public static EntityInfo getEmbryo(Animal animal) {
        return getMammalCap(animal).getEmbryo();
    }

    public static void setEmbryoProgress(Animal animal, int embryoProgress) {
        getMammalCap(animal).setEmbryoProgress(embryoProgress);
    }

    public static void setEmbryo(Animal animal, @Nullable EntityInfo embryo) {
        getMammalCap(animal).setEmbryo(embryo);
    }

    public static void syncMammalWithClient(Animal animal, int embryoProgress, EntityInfo embryo) {
        MessageHandler.CAP_CHANNEL.sendToPlayers(((ServerLevel) animal.level()).getPlayers(serverPlayer -> true),
                new S2CMammalCapMessage(animal, embryoProgress, embryo));
    }

    public static boolean hasHatchedDinosaur(Player player) {
        return getFirstHatchCap(player).hasHatchedDinosaur();
    }

    public static void setHatchedDinosaur(Player player, boolean hatched) {
        getFirstHatchCap(player).setHatchedDinosaur(hatched);
    }

    public static CommonEnergyStorage createEnergyStorage(Runnable setChanged) {
        return new FAEnergyStorage(FossilConfig.getInt(FossilConfig.MACHINE_MAX_ENERGY), FossilConfig.getInt(FossilConfig.MACHINE_TRANSFER_RATE), FossilConfig.getInt(FossilConfig.MACHINE_ENERGY_USAGE), 0) {
            @Override
            protected void onChange() {
                setChanged.run();
            }
        };
    }
}
