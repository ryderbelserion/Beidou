package com.ryderbelserion.discord.bot.guilds.config;

import com.ryderbelserion.discord.bot.guilds.config.types.MessageConfig;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.CommentedConfigurationNode;

public class GuildConfig {

    private final MessageConfig messageConfig;

    public GuildConfig(@NotNull final CommentedConfigurationNode config) {
        this.messageConfig = new MessageConfig(config.node("channels", "message-logs"));
    }

    public @NotNull final MessageConfig getMessageConfig() {
        return this.messageConfig;
    }
}