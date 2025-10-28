package com.ryderbelserion.discord.bot.guilds.config;

import com.ryderbelserion.discord.bot.guilds.config.types.MessageConfig;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.spongepowered.configurate.CommentedConfigurationNode;

public class GuildConfig {

    private final MessageConfig messageConfig;

    public GuildConfig(@NotNull final CommentedConfigurationNode config, @NotNull final Logger logger) {
        this.messageConfig = new MessageConfig(config.node("channels", "message-logs"), logger);
    }

    public @NotNull final MessageConfig getMessageConfig() {
        return this.messageConfig;
    }
}