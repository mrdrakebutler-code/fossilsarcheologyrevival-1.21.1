package com.github.teamfossilsarcheology.fossil.advancements;

import com.github.teamfossilsarcheology.fossil.FossilMod;
import net.minecraft.advancements.CriteriaTriggers;

public class ModTriggers {
    public static final OpenSarcophagusTrigger OPEN_SARCOPHAGUS_TRIGGER = CriteriaTriggers.register(FossilMod.location("open_sarcophagus").toString(), new OpenSarcophagusTrigger());
    public static final ScarabTameTrigger SCARAB_TAME_TRIGGER = CriteriaTriggers.register(FossilMod.location("scarab_tame").toString(), new ScarabTameTrigger());
    public static final ImplantEmbryoTrigger IMPLANT_EMBRYO_TRIGGER = CriteriaTriggers.register(FossilMod.location("implant_embryo").toString(), new ImplantEmbryoTrigger());
    public static final IncubateEggTrigger INCUBATE_EGG_TRIGGER = CriteriaTriggers.register(FossilMod.location("incubate_egg").toString(), new IncubateEggTrigger());

    public static void register() {
        //
    }
}
