package net.memeland.minecraftgov.networking;

import net.memeland.minecraftgov.ModgovMod;
import net.memeland.minecraftgov.networking.packet.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ModMessages {
    private static SimpleChannel INSTANCE;
    private static int packetId = 0;
    private static final Map<UUID, Map<Class<?>, Long>> packetCooldowns = new HashMap<>();
    private static final long PACKET_COOLDOWN_MS = 1000;

    private static int id() {
        return packetId++;
    }

    public static void register() {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(ResourceLocation.fromNamespaceAndPath(ModgovMod.MOD_ID, "messages"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE = net;

        net.messageBuilder(RegisterPartyPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(RegisterPartyPacket::new)
                .encoder(RegisterPartyPacket::toBytes)
                .consumerMainThread(RegisterPartyPacket::handle)
                .add();

        net.messageBuilder(VotePacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(VotePacket::new)
                .encoder(VotePacket::toBytes)
                .consumerMainThread(VotePacket::handle)
                .add();

        net.messageBuilder(VoteDataSyncPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(VoteDataSyncPacket::new)
                .encoder(VoteDataSyncPacket::toBytes)
                .consumerMainThread(VoteDataSyncPacket::handle)
                .add();

        net.messageBuilder(UpdatePlayerDataPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(UpdatePlayerDataPacket::new)
                .encoder(UpdatePlayerDataPacket::toBytes)
                .consumerMainThread(UpdatePlayerDataPacket::handle)
                .add();

        net.messageBuilder(PlayerDataSyncPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(PlayerDataSyncPacket::new)
                .encoder(PlayerDataSyncPacket::toBytes)
                .consumerMainThread(PlayerDataSyncPacket::handle)
                .add();

        net.messageBuilder(UpdatePamphletPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(UpdatePamphletPacket::new)
                .encoder(UpdatePamphletPacket::toBytes)
                .consumerMainThread(UpdatePamphletPacket::handle)
                .add();

        net.messageBuilder(SignPamphletPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(SignPamphletPacket::new)
                .encoder(SignPamphletPacket::toBytes)
                .consumerMainThread(SignPamphletPacket::handle)
                .add();
    }

    public static boolean isOnCooldown(UUID playerUuid, Class<?> packetClass) {
        Map<Class<?>, Long> playerCooldowns = packetCooldowns.computeIfAbsent(playerUuid, k -> new HashMap<>());
        Long lastSent = playerCooldowns.get(packetClass);
        long now = System.currentTimeMillis();

        if (lastSent != null && now - lastSent < PACKET_COOLDOWN_MS) {
            return true;
        }

        playerCooldowns.put(packetClass, now);
        return false;
    }

    public static void cleanupCooldowns() {
        long now = System.currentTimeMillis();
        packetCooldowns.entrySet().removeIf(entry -> {
            entry.getValue().entrySet().removeIf(cooldown ->
                    now - cooldown.getValue() > 300000); // Remove cooldowns older than 5 minutes
            return entry.getValue().isEmpty();
        });
    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }

    public static <MSG> void sendToClients(MSG message) {
        INSTANCE.send(PacketDistributor.ALL.noArg(), message);
    }
}