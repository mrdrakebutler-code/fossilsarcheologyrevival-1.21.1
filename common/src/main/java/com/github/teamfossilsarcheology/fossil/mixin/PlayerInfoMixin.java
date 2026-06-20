package com.github.teamfossilsarcheology.fossil.mixin;

import com.github.teamfossilsarcheology.fossil.client.FAPlayerCapes;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

@Mixin(PlayerInfo.class)
public class PlayerInfoMixin {

    @Shadow
    @Final
    private GameProfile profile;

    // 1.21: PlayerInfo dropped getCapeLocation()/getElytraLocation(). Cape and elytra textures now live
    // on the PlayerSkin record returned by getSkin(), so we substitute a copy with our custom textures.
    @Inject(method = "getSkin", at = @At("RETURN"), cancellable = true)
    private void fossilsArcheologyRevival$overrideCape(CallbackInfoReturnable<PlayerSkin> cir) {
        ResourceLocation cape = null;
        ResourceLocation elytra = null;
        if (fossilsArcheologyRevival$hasDevCape(profile.getId())) {
            cape = FAPlayerCapes.DEVELOPER_CAPE_TEXTURE;
            elytra = FAPlayerCapes.DEVELOPER_ELYTRA_TEXTURE;
        } else if (fossilsArcheologyRevival$hasContributorCape(profile.getId())) {
            cape = FAPlayerCapes.CONTRIBUTOR_CAPE_TEXTURE;
            elytra = FAPlayerCapes.CONTRIBUTOR_ELYTRA_TEXTURE;
        }
        if (cape != null) {
            PlayerSkin original = cir.getReturnValue();
            cir.setReturnValue(new PlayerSkin(original.texture(), original.textureUrl(), cape, elytra, original.model(), original.secure()));
        }
    }

    @Unique
    private boolean fossilsArcheologyRevival$hasDevCape(UUID playerId) {
        return FAPlayerCapes.DEV_UUIDS.contains(playerId);
    }

    @Unique
    private boolean fossilsArcheologyRevival$hasContributorCape(UUID playerId) {
        return FAPlayerCapes.CONTRIBUTOR_UUIDS.contains(playerId);
    }
}
