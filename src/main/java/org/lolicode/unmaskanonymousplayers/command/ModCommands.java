package org.lolicode.unmaskanonymousplayers.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.LiteralCommandNode;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.lolicode.unmaskanonymousplayers.UnmaskAnonymousPlayers;

public class ModCommands {
    private static void reload(ServerCommandSource source) {
        try {
            UnmaskAnonymousPlayers.CONFIG.load();
            source.sendFeedback(() -> Text.literal("Config reloaded").formatted(Formatting.GREEN), false);
        } catch (Exception e) {
            UnmaskAnonymousPlayers.LOGGER.error("Failed to load config", e);
            source.sendFeedback(() -> Text.literal("Failed to load config: " + e.getMessage()).formatted(Formatting.RED), false);
        }
    }
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralCommandNode<ServerCommandSource> root = CommandManager.literal("uap")
                .requires(Permissions.require("uap", 4))
                .build();
        LiteralCommandNode<ServerCommandSource> reloadNode = CommandManager.literal("reload")
                .requires(Permissions.require("uap.reload", 4))
                .executes(context -> {
                    reload(context.getSource());
                    return 0;
                })
                .build();
        root.addChild(reloadNode);
        dispatcher.getRoot().addChild(root);
    }
}
