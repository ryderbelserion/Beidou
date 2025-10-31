package com.ryderbelserion.discord.bot.guilds;

import com.ryderbelserion.discord.bot.guilds.features.logging.config.MessageConfig;
import com.ryderbelserion.discord.bot.guilds.features.threads.config.ThreadConfig;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.spongepowered.configurate.CommentedConfigurationNode;

public class GuildConfig {

    private final MessageConfig messageConfig;
    private final ThreadConfig threadConfig;

    public GuildConfig(@NotNull final CommentedConfigurationNode config, @NotNull final Logger logger) {
        this.messageConfig = new MessageConfig(config.node("settings", "channels", "message-logs"), logger);

        this.threadConfig = new ThreadConfig(config.node("settings", "threads", "creation"));
    }

    public @NotNull final MessageConfig getMessageConfig() {
        return this.messageConfig;
    }

    public @NotNull final ThreadConfig getThreadConfig() {
        return this.threadConfig;
    }
}