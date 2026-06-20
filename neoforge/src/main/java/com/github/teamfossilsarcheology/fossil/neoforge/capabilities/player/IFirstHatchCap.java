package com.github.teamfossilsarcheology.fossil.neoforge.capabilities.player;

import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;

public interface IFirstHatchCap extends INBTSerializable<CompoundTag> {
    boolean hasHatchedDinosaur();

    void setHatchedDinosaur(boolean hatched);
}
