package org.lolicode.unmaskanonymousplayers;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lolicode.unmaskanonymousplayers.command.ModCommands;
import org.lolicode.unmaskanonymousplayers.config.ModConfig;

public class UnmaskAnonymousPlayers implements DedicatedServerModInitializer {
    public static final String MOD_ID = "unmaskanonymousplayers";
    public static final String MOD_NAME = "Unmask Anonymous Players";
    public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);
    public static final ModConfig CONFIG = new ModConfig();

    @Override
    public void onInitializeServer() {
        try {
            CONFIG.load();
        } catch (Exception e) {
            LOGGER.error("Failed to load config", e);
        }
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> ModCommands.register(dispatcher));
    }
}
