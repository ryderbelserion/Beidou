package com.ryderbelserion.discord.api;

import com.ryderbelserion.discord.api.commands.CommandHandler;
import com.ryderbelserion.discord.api.listeners.StatusListener;
import com.ryderbelserion.fusion.addons.AddonManager;
import com.ryderbelserion.fusion.files.FileManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public abstract class DiscordPlugin {

    protected final String username;
    protected final Logger logger;

    private final JDA jda;

    public DiscordPlugin(
            @NotNull final List<GatewayIntent> intents,
            @NotNull final List<CacheFlag> flags,
            @NotNull final String username,
            @NotNull final Logger logger,
            @NotNull final String token
            ) {
        this.username = username;
        this.logger = logger;

        this.jda = JDABuilder.createDefault(token, intents)
                .enableCache(flags)
                .addEventListeners(new StatusListener(this))
                .build();
    }

    protected CommandHandler commandHandler;
    protected boolean isActive = false;
    protected FileManager fileManager;

    public abstract void onGuildReady(@NotNull final Guild guild);

    public void onReady(@NotNull final JDA jda) {
        this.commandHandler = new CommandHandler(jda);
    }

    public abstract void onReload(@NotNull final JDA jda);

    public abstract void onStop(@NotNull final JDA jda);

    public @NotNull final Path getGuildDirectory(@NotNull final String id) {
        return getGuildDirectory().resolve(id);
    }

    public @NotNull final FileManager getFileManager() {
        return this.fileManager;
    }

    public @NotNull final Path getGuildDirectory() {
        return getDirectory().resolve("guilds");
    }

    public @NotNull final Path getAddonDirectory() {
        return getDirectory().resolve("addons");
    }

    public @NotNull final Path getCacheDirectory() {
        return getDirectory().resolve("cache");
    }

    public @NotNull final String getUsername() {
        return this.username;
    }

    public @NotNull final Path getDirectory() {
        return Path.of("./%s".formatted(this.username));
    }

    public void addEventListener(@NotNull final Object... listeners) {
        this.jda.addEventListener(listeners);
    }

    public @NotNull final Logger getLogger() {
        return this.logger;
    }

    public final boolean isActive() {
        return this.isActive;
    }

    public void init() {
        final Path directory = getDirectory();

        try {
            if (!Files.exists(directory)) {
                Files.createDirectory(directory);
            }

            final Path guildDirectory = getGuildDirectory();

            if (!Files.exists(guildDirectory)) {
                Files.createDirectory(guildDirectory);
            }

            final Path addonDirectory = getAddonDirectory();

            if (!Files.exists(addonDirectory)) {
                Files.createDirectory(addonDirectory);
            }

            final Path cacheDirectory = getCacheDirectory();

            if (!Files.exists(cacheDirectory)) {
                Files.createDirectory(cacheDirectory);
            }
        } catch (final IOException exception) {
            exception.printStackTrace();
        }

        this.fileManager = new FileManager(directory);

        this.fileManager.compressFile(directory.resolve("logs").resolve("latest.log"), directory.resolve("logs"));

        this.logger.info("All ready to go!");

        this.isActive = true;
    }
}