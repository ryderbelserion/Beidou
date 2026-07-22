package com.ryderbelserion.discord.bot.configs.types;

import org.jspecify.annotations.NonNull;
import org.spongepowered.configurate.CommentedConfigurationNode;

public class FileConfig {

    private int recursionDepth;

    public void init(@NonNull final CommentedConfigurationNode config) {
        this.recursionDepth = config.node("root", "file", "recursion_depth").getInt(1);
    }

    public final int getRecursionDepth() {
        return this.recursionDepth;
    }
}