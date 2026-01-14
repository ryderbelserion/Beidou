package com.ryderbelserion.discord.bot.data;

import com.ryderbelserion.discord.bot.data.cloud.PostgresConnector;
import com.ryderbelserion.discord.bot.api.data.interfaces.IConnector;
import com.ryderbelserion.fusion.files.FileManager;
import org.jetbrains.annotations.NotNull;
import java.nio.file.Path;

public class DataManager {

    private final FileManager fileManager;

    private IConnector connector;

    public DataManager(@NotNull final FileManager fileManager) {
        this.fileManager = fileManager;
    }

    public final void init(@NotNull final Path directory) {
        this.connector = new PostgresConnector();
        this.connector.init(this.fileManager.getYamlFile(directory.resolve("config.yml")).orElseThrow().getConfiguration());
    }

    public @NotNull final IConnector getConnector() {
        return this.connector;
    }
}