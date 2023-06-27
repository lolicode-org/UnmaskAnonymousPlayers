package org.lolicode.unmaskanonymousplayers.mixin;

import com.mojang.authlib.GameProfile;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.NetworkState;
import net.minecraft.network.packet.c2s.handshake.HandshakeC2SPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.ServerMetadata;
import net.minecraft.server.network.ServerHandshakeNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerQueryNetworkHandler;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import org.lolicode.unmaskanonymousplayers.UnmaskAnonymousPlayers;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Optional;

@Mixin(ServerHandshakeNetworkHandler.class)
public class ServerHandshakeNetworkHandlerMixin {
    @Final
    @Shadow
    private MinecraftServer server;
    @Final
    @Shadow
    private ClientConnection connection;
    private final Random random = Random.create();
    @Inject(method = "onHandshake", at = @At("HEAD"), cancellable = true)
    private void checkIfIpInWhitelist(HandshakeC2SPacket packet, CallbackInfo ci) {
        if (packet.getIntendedState() != NetworkState.STATUS) {
            return;
        }
        String ip = "";
        var address = connection.getAddress();
        if (address instanceof InetSocketAddress) {
            ip = ((InetSocketAddress) address).getAddress().getHostAddress();
        }
        if (!UnmaskAnonymousPlayers.CONFIG.ipInWhitelist(ip)) {
            return;
        }
        ServerMetadata metadata = server.getServerMetadata();
        if (metadata != null) {  // ignores acceptsStatusQuery()
            var newMetadata = new ServerMetadata(
                    metadata.description(),
                    this.createMetadataPlayers(),
                    metadata.version(),
                    metadata.favicon(),
                    metadata.secureChatEnforced()
            );
            this.connection.setState(NetworkState.STATUS);
            this.connection.setPacketListener(new ServerQueryNetworkHandler(newMetadata, this.connection));
            ci.cancel();
        }
    }

    private Optional<ServerMetadata.Players> createMetadataPlayers() {
        PlayerManager playerManager = server.getPlayerManager();
        List<ServerPlayerEntity> list = playerManager.getPlayerList();
        int maxPlayerCount = playerManager.getMaxPlayerCount();
        int listSize = UnmaskAnonymousPlayers.CONFIG.showFullPlayerList ? list.size() : Math.min(list.size(), 12);
        ObjectArrayList<GameProfile> objectArrayList = new ObjectArrayList<>(listSize);
        int start = MathHelper.nextInt(this.random, 0, list.size() - listSize);

        for(int i = 0; i < listSize; ++i) {
            ServerPlayerEntity serverPlayerEntity = list.get(start + i);
            objectArrayList.add(serverPlayerEntity.getGameProfile());
        }

        if (UnmaskAnonymousPlayers.CONFIG.shufflePlayerList)
            Util.shuffle(objectArrayList, this.random);
        return Optional.of(new ServerMetadata.Players(maxPlayerCount, list.size(), objectArrayList));
    }
}
