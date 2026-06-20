package com.github.teamfossilsarcheology.fossil.mixin;

import com.github.teamfossilsarcheology.fossil.network.SyncedEntityDataHelper;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(SynchedEntityData.class)
public abstract class SyncedEntityDataMixin implements SyncedEntityDataHelper {

    // 1.21: SynchedEntityData replaced the Int2ObjectMap<DataItem> + ReadWriteLock internals with a
    // plain DataItem<?>[] indexed by accessor id. No lock field exists anymore.
    @Shadow
    @Final
    private SynchedEntityData.DataItem<?>[] itemsById;

    @Shadow
    private boolean isDirty;

    @Override
    public void fossilsArcheologyRevival$markNonDefaultAsDirty() {
        for (SynchedEntityData.DataItem<?> item : itemsById) {
            if (!item.isSetToDefault()) {
                item.setDirty(true);
                isDirty = true;
            }
        }
    }

    @Override
    public void fossilsArcheologyRevival$markDirty(EntityDataAccessor<Integer> dataAccessor) {
        itemsById[dataAccessor.id()].setDirty(true);
        isDirty = true;
    }
}
