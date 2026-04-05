package com.ryderbelserion.discord.bot.configs.types;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.CommentedConfigurationNode;

public class BotConfig {

    private final StorageConfig storageConfig;
    private final FileConfig fileConfig;

    private boolean isCustomStatusEnabled;
    private String customStatus;

    public BotConfig() {
        this.isCustomStatusEnabled = true;
        this.customStatus = "Watching {count} members";

        this.storageConfig = new StorageConfig();
        this.fileConfig = new FileConfig();
    }

    public void init(@NotNull final CommentedConfigurationNode config) {
        this.isCustomStatusEnabled = config.node("root", "presence", "toggle").getBoolean(true);
        this.customStatus = config.node("root", "presence", "status").getString("Watching {count} members");

        this.storageConfig.init(config.node("storage"));
        this.fileConfig.init(config);
    }

    public final boolean isCustomStatusEnabled() {
        return this.isCustomStatusEnabled;
    }

    public @NotNull final String getCustomStatus() {
        return this.customStatus;
    }

    public @NotNull final StorageConfig getStorageConfig() {
        return this.storageConfig;
    }

    public @NotNull final FileConfig getFileConfig() {
        return this.fileConfig;
    }
}