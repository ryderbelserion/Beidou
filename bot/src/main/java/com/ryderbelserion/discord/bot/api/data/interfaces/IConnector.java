package com.ryderbelserion.discord.bot.api.data.interfaces;

import org.jspecify.annotations.NonNull;
import org.spongepowered.configurate.CommentedConfigurationNode;
import java.nio.file.Path;

public interface IConnector {

    void init(@NonNull final CommentedConfigurationNode config);

    void stop();

    default void reload() {

    }

    default Path getPath() {
        return null;
    }

    String getImpl();

}