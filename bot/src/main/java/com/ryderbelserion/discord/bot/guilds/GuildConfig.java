package com.ryderbelserion.discord.bot.guilds;

import com.ryderbelserion.discord.bot.api.managers.EmbedManager;
import com.ryderbelserion.discord.bot.guilds.features.logging.config.MessageConfig;
import com.ryderbelserion.discord.bot.guilds.features.threads.config.ThreadConfig;
import com.ryderbelserion.discord.bot.guilds.features.traffic.config.TrafficConfig;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.spongepowered.configurate.CommentedConfigurationNode;

public class GuildConfig {

    private final MessageConfig messageConfig;
    private final TrafficConfig trafficConfig;
    private final ThreadConfig threadConfig;

    private final String defaultAvatar;

    public GuildConfig(
            @NonNull final CommentedConfigurationNode config,
            @NonNull final CommentedConfigurationNode traffic,
            @NonNull final EmbedManager embedManager,
            @NonNull final Logger logger
    ) {
        this.messageConfig = new MessageConfig(config.node("settings", "channels", "track-messages"), embedManager, logger);

        this.trafficConfig = new TrafficConfig(traffic.node("traffic"), embedManager);

        this.threadConfig = new ThreadConfig(config.node("settings", "threads", "creation"));

        this.defaultAvatar = config.node("settings", "default-avatar").getString("https://raw.githubusercontent.com/ryderbelserion/Beidou/refs/heads/main/pout.jpg");
    }

    public @NonNull final MessageConfig getMessageConfig() {
        return this.messageConfig;
    }

    public @NonNull final TrafficConfig getTrafficConfig() {
        return this.trafficConfig;
    }

    public @NonNull final ThreadConfig getThreadConfig() {
        return this.threadConfig;
    }

    public @NonNull final String getDefaultAvatar() {
        return this.defaultAvatar;
    }
}