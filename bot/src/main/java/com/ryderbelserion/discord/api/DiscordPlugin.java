package com.ryderbelserion.discord.api;

import com.ryderbelserion.discord.api.commands.CommandContext;
import com.ryderbelserion.discord.api.listeners.StatusListener;
import com.ryderbelserion.fusion.files.FileManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public abstract class DiscordPlugin {

    protected final String username;
    protected final Logger logger;

    private final JDA jda;

    public DiscordPlugin(
            @NonNull final List<GatewayIntent> intents,
            @NonNull final List<CacheFlag> flags,
            @NonNull final String username,
            @NonNull final Logger logger,
            @NonNull final String token
            ) {
        this.username = username;
        this.logger = logger;

        this.jda = JDABuilder.createDefault(token, intents)
                .enableCache(flags)
                .addEventListeners(new StatusListener(this))
                .build();
    }

    protected boolean isActive = false;
    protected FileManager fileManager;

    public abstract void onGuildReady(@NonNull final Guild guild, final boolean isReload);

    public abstract void onReload(@NonNull final String id, @NonNull final CommandContext user);

    public abstract void onReload(@NonNull final JDA jda);

    public abstract void onReady(@NonNull final JDA jda);

    public abstract void onStop(@NonNull final JDA jda);

    public @NonNull final Path getGuildDirectory(@NonNull final String id) {
        return getGuildDirectory().resolve(id);
    }

    public @NonNull final FileManager getFileManager() {
        return this.fileManager;
    }

    public @NonNull final Path getGuildDirectory() {
        return getDirectory().resolve("guilds");
    }

    public @NonNull final Path getAddonDirectory() {
        return getDirectory().resolve("addons");
    }

    public @NonNull final Path getCacheDirectory() {
        return getDirectory().resolve("cache");
    }

    public @NonNull final Path getCommandsDirectory(@NonNull final String id) {
        return getGuildDirectory(id).resolve("commands");
    }

    public @NonNull final Path getEmbedsDirectory(@NonNull final String id) {
        return getGuildDirectory(id).resolve("embeds");
    }

    public @NonNull final String getUsername() {
        return this.username;
    }

    public @NonNull final Path getDirectory() {
        return Path.of("./%s".formatted(this.username));
    }

    public String replacePlaceholder(@NonNull final String message, @NonNull final Map<String, String> placeholders) {
        String safeMessage = message;

        if (!placeholders.isEmpty()) {
            for (final Map.Entry<String, String> key : placeholders.entrySet()) {
                if (key == null) continue;

                final String placeholder = key.getKey();
                final String value = key.getValue();

                if (placeholder != null && value != null) {
                    safeMessage = safeMessage.replace(placeholder, value).replace(placeholder.toLowerCase(), value);
                }
            }
        }

        return safeMessage;
    }

    public void addEventListener(@NonNull final Object... listeners) {
        this.jda.addEventListener(listeners);
    }

    public @NonNull final Logger getLogger() {
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

        this.logger.info("All ready to go!");

        this.isActive = true;
    }
}