package com.ryderbelserion.discord.bot.configs.types;

import com.ryderbelserion.discord.bot.storage.StorageCredentials;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurationNode;

public class StorageConfig {

    private StorageCredentials credentials;

    private String type;

    public void init(@NotNull final CommentedConfigurationNode config) {
        final String type = config.node("type").getString("SQLITE");

        final int port = config.node("connection", "port").getInt(switch (type) {
            case "POSTGRESQL" -> 5432;
            case "MYSQL" -> 3306;
            default -> -1;
        });

        final ConfigurationNode settings = config.node("pool-settings");

        this.credentials = new StorageCredentials(
                config.node("database").getString(""),
                config.node("username").getString(""),
                config.node("password").getString(""),
                config.node("connection", "url").getString(""),
                settings.node("connection-timeout").getInt(5000),
                settings.node("maximum-lifetime").getInt(1800000),
                settings.node("maximum-pool-size").getInt(10),
                settings.node("keepalive-time").getInt(0),
                settings.node("minimum-idle").getInt(10),
                port
        );

        this.type = type.toLowerCase();
    }

    public @NotNull final StorageCredentials getCredentials() {
        return this.credentials;
    }

    public @NotNull final String getType() {
        return this.type;
    }
}