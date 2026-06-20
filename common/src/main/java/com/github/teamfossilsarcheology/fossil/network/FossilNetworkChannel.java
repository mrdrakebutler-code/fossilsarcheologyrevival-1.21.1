package com.github.teamfossilsarcheology.fossil.network;

import com.google.common.collect.Maps;
import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.NetworkManager.PacketContext;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Drop-in replacement for architectury's {@link dev.architectury.networking.NetworkChannel} that registers each
 * message on a SINGLE direction.
 * <p>
 * architectury's {@code NetworkChannel.register} registers every message as both C2S and (on the client) S2C under
 * the same payload id. On Forge that was fine, but NeoForge keys payloads by {@code (ConnectionProtocol, id)} and
 * rejects a second registration of the same id in the PLAY protocol — so the bidirectional registration crashes with
 * "Cannot register payload &lt;id&gt; as it is already registered." (see {@code NetworkRegistry.register}).
 * <p>
 * Every message in this mod has an unambiguous direction encoded in its class name ({@code S2C*} = server→client,
 * everything else = client→server), so we register only the receiving side. The payload id matches architectury's
 * scheme exactly ({@code <channel>/<md5(class name)>}) so encode/decode line up across the wire.
 */
public final class FossilNetworkChannel {
    private final ResourceLocation id;
    private final Map<Class<?>, Entry> entries = Maps.newHashMap();

    private FossilNetworkChannel(ResourceLocation id) {
        this.id = id;
    }

    public static FossilNetworkChannel create(ResourceLocation id) {
        return new FossilNetworkChannel(id);
    }

    @SuppressWarnings("unchecked")
    public <T> void register(Class<T> type, BiConsumer<T, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, T> decoder,
                             BiConsumer<T, Supplier<PacketContext>> messageConsumer) {
        // Mirror architectury's id derivation so server-encoded and client-decoded ids match.
        String hash = UUID.nameUUIDFromBytes(type.getName().getBytes(StandardCharsets.UTF_8)).toString().replace("-", "");
        ResourceLocation packetId = ResourceLocation.parse(id + "/" + hash);
        NetworkManager.Side side = type.getSimpleName().startsWith("S2C") ? NetworkManager.s2c() : NetworkManager.c2s();
        entries.put(type, new Entry(packetId, (BiConsumer<Object, FriendlyByteBuf>) encoder));

        NetworkManager.NetworkReceiver<RegistryFriendlyByteBuf> receiver = (buf, context) ->
                messageConsumer.accept(decoder.apply(buf), () -> context);
        NetworkManager.registerReceiver(side, packetId, Collections.emptyList(), receiver);
    }

    public <T> void sendToPlayer(ServerPlayer player, T message) {
        Objects.requireNonNull(player, "Unable to send packet to a 'null' player!");
        Entry entry = entry(message);
        RegistryFriendlyByteBuf buf = new RegistryFriendlyByteBuf(Unpooled.buffer(), player.registryAccess());
        entry.encoder.accept(message, buf);
        NetworkManager.sendToPlayer(player, entry.packetId, buf);
    }

    public <T> void sendToPlayers(Iterable<ServerPlayer> players, T message) {
        Iterator<ServerPlayer> iterator = players.iterator();
        if (!iterator.hasNext()) {
            return;
        }
        Entry entry = entry(message);
        RegistryFriendlyByteBuf buf = new RegistryFriendlyByteBuf(Unpooled.buffer(), iterator.next().registryAccess());
        entry.encoder.accept(message, buf);
        NetworkManager.sendToPlayers(players, entry.packetId, buf);
    }

    /** Client only. References to {@link Minecraft} resolve lazily, so this class still loads on a dedicated server. */
    public <T> void sendToServer(T message) {
        var connection = Minecraft.getInstance().getConnection();
        if (connection == null) {
            throw new IllegalStateException("Unable to send packet to the server while not in game!");
        }
        Entry entry = entry(message);
        RegistryFriendlyByteBuf buf = new RegistryFriendlyByteBuf(Unpooled.buffer(), connection.registryAccess());
        entry.encoder.accept(message, buf);
        NetworkManager.sendToServer(entry.packetId, buf);
    }

    private Entry entry(Object message) {
        return Objects.requireNonNull(entries.get(message.getClass()), () -> "Unknown message type! " + message);
    }

    private record Entry(ResourceLocation packetId, BiConsumer<Object, FriendlyByteBuf> encoder) {
    }
}
