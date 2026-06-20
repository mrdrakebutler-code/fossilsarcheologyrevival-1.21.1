package com.github.teamfossilsarcheology.fossil.neoforge.capabilities;

import com.github.teamfossilsarcheology.fossil.FossilMod;
import com.github.teamfossilsarcheology.fossil.neoforge.capabilities.mammal.MammalCap;
import com.github.teamfossilsarcheology.fossil.neoforge.capabilities.player.FirstHatchCap;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

/**
 * NeoForge replaces Forge capabilities with data attachments. The mammal/firstHatch capability data
 * (formerly attached via {@code AttachCapabilitiesEvent}) is now lazily created on any holder when first
 * accessed and persisted through the attachment serializer.
 *
 * @see com.github.teamfossilsarcheology.fossil.capabilities.ModCapabilities
 */
public class ModAttachments {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, FossilMod.MOD_ID);

    public static final Supplier<AttachmentType<MammalCap>> MAMMAL =
            ATTACHMENT_TYPES.register("mammal", () -> AttachmentType.serializable(MammalCap::new).build());

    public static final Supplier<AttachmentType<FirstHatchCap>> FIRST_HATCH =
            ATTACHMENT_TYPES.register("first_hatch", () -> AttachmentType.serializable(FirstHatchCap::new).copyOnDeath().build());
}
