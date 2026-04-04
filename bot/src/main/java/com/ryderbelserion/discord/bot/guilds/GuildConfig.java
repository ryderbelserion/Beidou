package com.ryderbelserion.discord.bot.guilds;

import com.ryderbelserion.discord.bot.api.managers.EmbedManager;
import com.ryderbelserion.discord.bot.guilds.features.logging.config.MessageConfig;
import com.ryderbelserion.discord.bot.guilds.features.threads.config.ThreadConfig;
import com.ryderbelserion.discord.bot.guilds.features.traffic.config.TrafficConfig;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.spongepowered.configurate.CommentedConfigurationNode;

public class GuildConfig {

    private final MessageConfig messageConfig;
    private final TrafficConfig trafficConfig;
    private final ThreadConfig threadConfig;

    public GuildConfig(
            @NotNull final CommentedConfigurationNode config,
            @NotNull final CommentedConfigurationNode traffic,
            @NotNull final EmbedManager embedManager,
            @NotNull final Logger logger
    ) {
        this.messageConfig = new MessageConfig(config.node("settings", "channels", "track-messages"), embedManager, logger);

        this.trafficConfig = new TrafficConfig(traffic.node("traffic"), embedManager);

        this.threadConfig = new ThreadConfig(config.node("settings", "threads", "creation"));
    }

    public @NotNull final MessageConfig getMessageConfig() {
        return this.messageConfig;
    }

    public @NotNull final TrafficConfig getTrafficConfig() {
        return this.trafficConfig;
    }

    public @NotNull final ThreadConfig getThreadConfig() {
        return this.threadConfig;
    }
}