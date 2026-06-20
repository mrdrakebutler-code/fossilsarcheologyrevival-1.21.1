package com.github.teamfossilsarcheology.fossil.neoforge.energy;

import com.github.teamfossilsarcheology.fossil.block.entity.CommonEnergyStorage;
import net.minecraft.nbt.Tag;
import net.neoforged.neoforge.energy.EnergyStorage;

public abstract class FAEnergyStorage extends EnergyStorage implements CommonEnergyStorage {

    protected FAEnergyStorage(int capacity, int maxReceive, int maxExtract, int energy) {
        super(capacity, maxReceive, maxExtract, energy);
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        int received = super.receiveEnergy(maxReceive, simulate);
        if (received != 0) {
            onChange();
        }
        return received;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        int extracted = super.extractEnergy(maxExtract, simulate);
        if (extracted != 0) {
            onChange();
        }
        return extracted;
    }

    protected abstract void onChange();

    @Override
    public int getEnergy() {
        return getEnergyStored();
    }

    @Override
    public void extractEnergy(int maxExtract) {
        extractEnergy(maxExtract, false);
    }

    @Override
    public void load(Tag tag) {
        // The energy is persisted as a single numeric tag, so the HolderLookup.Provider is unused here.
        if (tag == null) {
            energy = 0;
        } else {
            deserializeNBT(null, tag);
        }
    }

    @Override
    public Tag save() {
        return serializeNBT(null);
    }
}
