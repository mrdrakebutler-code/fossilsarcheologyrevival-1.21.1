package com.github.teamfossilsarcheology.fossil.neoforge.event;

import com.github.teamfossilsarcheology.fossil.FossilMod;
import com.github.teamfossilsarcheology.fossil.capabilities.ModCapabilities;
import com.github.teamfossilsarcheology.fossil.capabilities.neoforge.ModCapabilitiesImpl;
import com.github.teamfossilsarcheology.fossil.config.FossilConfig;
import com.github.teamfossilsarcheology.fossil.entity.prehistoric.base.PrehistoricEntityInfo;
import com.github.teamfossilsarcheology.fossil.event.ModEvents;
import com.github.teamfossilsarcheology.fossil.neoforge.capabilities.mammal.IMammalCap;
import com.github.teamfossilsarcheology.fossil.network.MessageHandler;
import com.github.teamfossilsarcheology.fossil.network.S2CMammalCapMessage;
import com.github.teamfossilsarcheology.fossil.villager.ModTrades;
import com.github.teamfossilsarcheology.fossil.villager.ModVillagers;
import com.github.teamfossilsarcheology.fossil.world.effect.ComfyBedEffect;
import com.github.teamfossilsarcheology.fossil.world.effect.ModEffects;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.animal.Animal;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import net.neoforged.neoforge.event.entity.player.CanContinueSleepingEvent;
import net.neoforged.neoforge.event.entity.player.CanPlayerSleepEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerWakeUpEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;

import java.util.List;
import java.util.Optional;

@EventBusSubscriber(modid = FossilMod.MOD_ID)
public class NeoForgeModEvents {

    @SubscribeEvent
    public static void addCustomTrades(VillagerTradesEvent event) {
        if (event.getType() == ModVillagers.ARCHEOLOGIST.get()) {
            var trades = event.getTrades();
            trades.get(1).addAll(ModTrades.getArcheoList(1));
            trades.get(2).addAll(ModTrades.getArcheoList(2));
            trades.get(3).addAll(ModTrades.getArcheoList(3));
            trades.get(4).addAll(ModTrades.getArcheoList(4));
            trades.get(5).addAll(ModTrades.getArcheoList(5));
        } else if (event.getType() == ModVillagers.PALEONTOLOGIST.get()) {
            var trades = event.getTrades();
            trades.get(1).addAll(ModTrades.getPaleoList(1));
            trades.get(2).addAll(ModTrades.getPaleoList(2));
            trades.get(3).addAll(ModTrades.getPaleoList(3));
            trades.get(4).addAll(ModTrades.getPaleoList(4));
            trades.get(5).addAll(ModTrades.getPaleoList(5));
        }
    }

    @SubscribeEvent
    public static void onLivingUpdate(EntityTickEvent.Post event) {
        if (event.getEntity() instanceof Animal animal) {
            int currentProgress = ModCapabilities.getEmbryoProgress(animal);
            if (currentProgress == 0) {
                return;
            }
            if (currentProgress >= FossilConfig.getInt(FossilConfig.PREGNANCY_DURATION)) {
                if (!animal.level().isClientSide) {
                    ModEvents.growEntity(ModCapabilities.getEmbryo(animal), animal);
                    ModCapabilities.stopPregnancy(animal);
                }
            } else {
                ModCapabilities.setEmbryoProgress(animal, currentProgress + 1);
            }
        }
    }

    // Mammal/firstHatch data is now NeoForge data attachments (lazily created + auto-copied via copyOnDeath),
    // so the old AttachCapabilitiesEvent attach + PlayerEvent.Clone copy handlers are no longer needed.

    @SubscribeEvent
    public static void onPlayerStartTracking(PlayerEvent.StartTracking event) {
        ServerPlayer serverPlayer = (ServerPlayer) event.getEntity();
        if (event.getTarget() instanceof Animal animal && PrehistoricEntityInfo.isMammal(animal)) {
            IMammalCap cap = ModCapabilitiesImpl.getMammalCap(animal);
            MessageHandler.CAP_CHANNEL.sendToPlayers(List.of(serverPlayer),
                    new S2CMammalCapMessage(animal, cap.getEmbryoProgress(), cap.getEmbryo()));
        }
    }

    @SubscribeEvent
    public static void allowDaySleep(CanPlayerSleepEvent event) {
        if (ComfyBedEffect.canApply(Optional.of(event.getPos()), event.getEntity().level())) {
            event.setProblem(null);
        }
    }

    @SubscribeEvent
    public static void allowDaySleep(CanContinueSleepingEvent event) {
        if (ComfyBedEffect.canApply(event.getEntity().getSleepingPos(), event.getEntity().level())) {
            event.setContinueSleeping(true);
        }
    }

    @SubscribeEvent
    public static void addComfyBedEffect(PlayerWakeUpEvent event) {
        if (ComfyBedEffect.canApply(event.getEntity().getSleepingPos(), event.getEntity().level())) {
            event.getEntity().addEffect(new MobEffectInstance(ModEffects.COMFY_BED, 24000, 0));
        }
    }

    @SubscribeEvent
    public static void addComfyBedEffect(LivingEvent.LivingVisibilityEvent event) {
        if (event.getEntity().hasEffect(ModEffects.COMFY_BED)) {
            event.modifyVisibility(0.5);
        }
    }

    @SubscribeEvent
    public static void onDatapackSyncEvent(OnDatapackSyncEvent event) {
        if (event.getPlayer() != null) {
            FossilMod.syncData(event.getPlayer());
        } else {
            for (ServerPlayer player : event.getPlayerList().getPlayers()) {
                FossilMod.syncData(player);
            }
        }
    }
}
