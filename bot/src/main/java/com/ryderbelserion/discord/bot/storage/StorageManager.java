package com.ryderbelserion.discord.bot.storage;

import com.ryderbelserion.discord.bot.Beidou;
import com.ryderbelserion.discord.bot.configs.ConfigManager;
import com.ryderbelserion.discord.bot.configs.types.StorageConfig;
import com.ryderbelserion.discord.bot.storage.impl.file.SqliteFactory;
import com.ryderbelserion.discord.bot.storage.impl.hikari.PostgresFactory;
import com.ryderbelserion.discord.bot.storage.impl.objects.StorageHolder;
import org.jetbrains.annotations.NotNull;
import java.nio.file.Path;

public class StorageManager {

    private final ConfigManager configManager;
    private final Beidou instance;
    private final Path path;

    public StorageManager(@NotNull final Beidou instance) {
        this.configManager = instance.getConfigManager();
        this.path = instance.getDirectory();
        this.instance = instance;
    }

    public StorageHolder init() throws Exception {
        final StorageConfig config = this.configManager.getConfig().getStorageConfig();

        final StorageCredentials credentials = config.getCredentials();

        final String type = config.getType();

        // table #1 which contains guild ids
        // table #2 with the guild id which contains user data

        return switch (type) {
            case "postgres" -> new StorageHolder(new PostgresFactory(credentials)).initialize();
            case "sqlite" -> new StorageHolder(new SqliteFactory(this.path.resolve("beidou.db"))).initialize();
            default -> throw new IllegalStateException("Unknown Database Type: %s".formatted(type));
        };
    }
}