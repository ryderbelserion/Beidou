package com.ryderbelserion.discord.bot.configs.types;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.CommentedConfigurationNode;

public class BotConfig {

    private final boolean isCustomStatusEnabled;
    private final String customStatus;

    public BotConfig(@NotNull final CommentedConfigurationNode config) {
        this.isCustomStatusEnabled = config.node("root", "presence", "toggle").getBoolean(true);
        this.customStatus = config.node("root", "presence", "status").getString("Watching {count} members");
    }

    public final boolean isCustomStatusEnabled() {
        return this.isCustomStatusEnabled;
    }

    public @NotNull final String getCustomStatus() {
        return this.customStatus;
    }
}