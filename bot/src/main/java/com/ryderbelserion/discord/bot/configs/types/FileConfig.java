package com.ryderbelserion.discord.bot.configs.types;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.CommentedConfigurationNode;

public class FileConfig {

    private int recursionDepth;

    public void init(@NotNull final CommentedConfigurationNode config) {
        this.recursionDepth = config.node("root", "file", "recursion_depth").getInt(1);
    }

    public final int getRecursionDepth() {
        return this.recursionDepth;
    }
}