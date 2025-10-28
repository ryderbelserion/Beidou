package com.ryderbelserion.discord.bot.configs;

import com.ryderbelserion.discord.bot.configs.types.GuildCache;
import com.ryderbelserion.fusion.files.FileManager;
import org.jetbrains.annotations.NotNull;
import java.nio.file.Path;

public class ConfigManager {

    private final FileManager fileManager;
    private final Path path;

    private GuildCache guildCache;

    public ConfigManager(@NotNull final FileManager fileManager, @NotNull final Path path) {
        this.fileManager = fileManager;
        this.path = path;
    }

    public void init() {
        this.fileManager.getJsonFile(this.path.resolve("guilds.json")).ifPresent(file -> {
            this.guildCache = new GuildCache(file);
            this.guildCache.init();
        });
    }

    public void reload() {
        this.fileManager.refresh(false);

        this.guildCache.init();
    }

    public @NotNull final GuildCache getGuildCache() {
        return this.guildCache;
    }
}