package com.ryderbelserion.discord.bot.configs;

import com.ryderbelserion.discord.bot.configs.types.BotConfig;
import com.ryderbelserion.discord.bot.configs.types.guilds.GuildCache;
import com.ryderbelserion.fusion.files.FileManager;
import org.jetbrains.annotations.NotNull;
import java.nio.file.Path;

public class ConfigManager {

    private final FileManager fileManager;
    private final Path path;

    private GuildCache guildCache;
    private BotConfig config;

    public ConfigManager(@NotNull final FileManager fileManager, @NotNull final Path path) {
        this.fileManager = fileManager;
        this.path = path;
    }

    public void init() {
        this.fileManager.getJsonFile(this.path.resolve("guilds.json")).ifPresent(file -> {
            this.guildCache = new GuildCache();
            this.guildCache.init(file.getConfiguration());
        });

        this.fileManager.getYamlFile(this.path.resolve("config.yml")).ifPresent(file -> {
            this.config = new BotConfig();
            this.config.init(file.getConfiguration());
        });
    }

    public void reload() {
        this.fileManager.refresh(false);

        this.fileManager.getJsonFile(this.path.resolve("guilds.json")).ifPresent(file -> this.guildCache.init(file.getConfiguration()));

        this.fileManager.getYamlFile(this.path.resolve("config.yml")).ifPresent(file -> this.config.init(file.getConfiguration()));
    }

    public @NotNull final GuildCache getGuildCache() {
        return this.guildCache;
    }

    public @NotNull final BotConfig getConfig() {
        return this.config;
    }
}