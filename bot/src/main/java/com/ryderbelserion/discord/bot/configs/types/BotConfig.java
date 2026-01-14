package com.ryderbelserion.discord.bot.configs.types;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.CommentedConfigurationNode;

public class BotConfig {

    //private final String storageType;

    //private final String Url;
    //private final String port;

    //private final String database;
    //private final String username;
    //private final String password;

    private final int maximumPoolSize;
    private final int minimumIdle;

    private final long maximumLifeTime;
    private final long keepaliveTime;
    private final long connectionTimeout;


    public BotConfig(@NotNull final CommentedConfigurationNode config) {
        this.maximumPoolSize = config.node("root", "storage", "pool-settings", "maximum-pool-size").getInt(10);
        this.minimumIdle = config.node("root", "storage", "pool-settings", "minimum-idle").getInt(10);
        this.maximumLifeTime = config.node("root", "storage", "pool-settings", "maximum-lifetime").getLong(1800000);

        this.keepaliveTime = config.node("root", "storage", "pool-settings", "keepalive-time").getLong(0);

        this.connectionTimeout = config.node("root", "storage", "pool-settings", "connection-timeout").getLong(5000);
    }
}