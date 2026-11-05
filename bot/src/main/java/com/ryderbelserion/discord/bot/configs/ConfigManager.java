package com.ryderbelserion.discord.bot.configs;

import com.ryderbelserion.discord.bot.configs.types.BotConfig;
import com.ryderbelserion.discord.bot.configs.types.guilds.GuildCache;
import com.ryderbelserion.fusion.files.FileManager;
import org.jspecify.annotations.NonNull;
import java.nio.file.Path;

public class ConfigManager {

    private final FileManager fileManager;
    private final Path path;

    private GuildCache guildCache;
    private BotConfig config;

    public ConfigManager(@NonNull final FileManager fileManager, @NonNull final Path path) {
        this.fileManager = fileManager;
        this.path = path;
    }

    public void init() {
        this.fileManager.getYamlFile(this.path.resolve("config.yml")).ifPresent(file -> {
            this.config = new BotConfig();
            this.config.init(file.getConfig());
        });
    }

    public void reload() {
        this.fileManager.refresh(false);

        this.fileManager.getYamlFile(this.path.resolve("config.yml")).ifPresent(file -> this.config.init(file.getConfig()));
    }

    public @NonNull final GuildCache getGuildCache() {
        return this.guildCache;
    }

    public @NonNull final BotConfig getConfig() {
        return this.config;
    }
}