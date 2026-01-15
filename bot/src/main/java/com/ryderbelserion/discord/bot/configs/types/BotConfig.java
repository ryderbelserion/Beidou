package com.ryderbelserion.discord.bot.configs.types;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.CommentedConfigurationNode;

public class BotConfig {

    private boolean isCustomStatusEnabled;
    private String customStatus;

    public BotConfig() {
        this.isCustomStatusEnabled = true;
        this.customStatus = "Watching {count} members";
    }

    public void init(@NotNull final CommentedConfigurationNode config) {
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