package org.lolicode.unmaskanonymousplayers.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import net.fabricmc.loader.api.FabricLoader;
import org.lolicode.unmaskanonymousplayers.UnmaskAnonymousPlayers;
import org.lolicode.unmaskanonymousplayers.utils.IpAddressMatcher;
import org.lolicode.unmaskanonymousplayers.utils.MatcherWrapper;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class ModConfig {
    @Expose
    private final List<String> whitelist = new LinkedList<>();
    @Expose
    public volatile boolean showFullPlayerList = false;
    @Expose
    public volatile boolean shufflePlayerList = true; // Vanilla behavior
    private final List<IpAddressMatcher> whitelistMatchers = new LinkedList<>();
    private static final File configFile = new File(FabricLoader.getInstance().getConfigDir().toFile(), UnmaskAnonymousPlayers.MOD_ID + ".json");
    private static final Gson GSON = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();

    public synchronized void load() {
        whitelist.clear();
        whitelistMatchers.clear();
        showFullPlayerList = false;
        shufflePlayerList = true;
        if (!configFile.exists()) {
            initialize();
        }
        if (!configFile.isFile()) {
            throw new RuntimeException("Config file (" + configFile.getAbsolutePath() + ") is not a file");
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(configFile))) {
            ModConfig config = GSON.fromJson(reader, ModConfig.class);
            if (config != null) {
                showFullPlayerList = config.showFullPlayerList;
                shufflePlayerList = config.shufflePlayerList;
                whitelist.addAll(config.whitelist);
                whitelist.stream().map(MatcherWrapper::getMatcher).filter(Optional::isPresent).map(Optional::get).forEach(whitelistMatchers::add);
            }
        } catch (IOException e) {
            UnmaskAnonymousPlayers.LOGGER.error("Failed to load mod config", e);
        }
    }

    public synchronized void initialize() {
        try (FileWriter writer = new FileWriter(configFile)) {
            writer.write(GSON.toJson(this));
        } catch (IOException e) {
            UnmaskAnonymousPlayers.LOGGER.error("Failed to initialize mod config", e);
        }
    }

    public synchronized boolean ipInWhitelist(String ip) {
        return whitelistMatchers.stream().anyMatch(matcher -> MatcherWrapper.matches(matcher, ip));
    }
}
