package com.github.teamfossilsarcheology.fossil.neoforge.capabilities.player;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;

public class FirstHatchCap implements IFirstHatchCap {
    private boolean hatchedDinosaur;

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("HatchedDinosaur", hatchedDinosaur);
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag tag) {
        setHatchedDinosaur(tag.getBoolean("HatchedDinosaur"));
    }

    @Override
    public boolean hasHatchedDinosaur() {
        return hatchedDinosaur;
    }

    @Override
    public void setHatchedDinosaur(boolean hatched) {
        this.hatchedDinosaur = hatched;
    }
}
