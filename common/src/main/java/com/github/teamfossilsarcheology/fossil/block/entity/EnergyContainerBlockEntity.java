package com.github.teamfossilsarcheology.fossil.block.entity;

import com.github.teamfossilsarcheology.fossil.capabilities.ModCapabilities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class EnergyContainerBlockEntity extends MachineContainerBlockEntity {
    protected final CommonEnergyStorage energyStorage = ModCapabilities.createEnergyStorage(this::setChanged);

    protected EnergyContainerBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        if (tag.contains("Energy")) {
            energyStorage.load(tag.get("Energy"));
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        tag.put("Energy", energyStorage.save());
    }

    public CommonEnergyStorage getEnergyStorage() {
        return energyStorage;
    }
}
