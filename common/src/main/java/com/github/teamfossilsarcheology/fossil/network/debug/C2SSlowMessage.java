package com.github.teamfossilsarcheology.fossil.network.debug;

import com.github.teamfossilsarcheology.fossil.FossilMod;
import com.github.teamfossilsarcheology.fossil.network.FossilNetworkChannel;
import dev.architectury.networking.NetworkManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

import java.util.function.Supplier;

public class C2SSlowMessage {
    private final double modifier;

    private C2SSlowMessage(FriendlyByteBuf buf) {
        this(buf.readDouble());
    }

    public C2SSlowMessage(double modifier) {
        this.modifier = modifier;
    }

    private void write(FriendlyByteBuf buf) {
        buf.writeDouble(modifier);
    }

    private void apply(Supplier<NetworkManager.PacketContext> contextSupplier) {
        contextSupplier.get().queue(() -> {
            Player player = contextSupplier.get().getPlayer();
            if (!player.level().isClientSide) {
                var instance = player.getAttribute(Attributes.MOVEMENT_SPEED);
                instance.removeModifiers();

                AttributeModifier attributeModifier = new AttributeModifier(FossilMod.location("debug_slow_down"), -(1-modifier), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
                instance.addPermanentModifier(attributeModifier);
            }
        });
    }

    public static void register(FossilNetworkChannel channel) {
        channel.register(C2SSlowMessage.class, C2SSlowMessage::write, C2SSlowMessage::new, C2SSlowMessage::apply);
    }
}
