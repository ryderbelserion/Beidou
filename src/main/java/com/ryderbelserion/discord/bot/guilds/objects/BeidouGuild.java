package com.ryderbelserion.discord.bot.guilds.objects;

import com.ryderbelserion.discord.bot.guilds.config.GuildConfig;
import com.ryderbelserion.fusion.files.FileManager;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.spongepowered.configurate.CommentedConfigurationNode;
import java.nio.file.Path;

public class BeidouGuild {

    private CommentedConfigurationNode config;
    private GuildConfig guildConfig;

    public BeidouGuild(@NotNull final FileManager fileManager, @NotNull final Logger logger, @NotNull final Path directory) {
        fileManager.getYamlFile(directory.resolve("config.yml")).ifPresent(customFile -> {
            this.config = customFile.getConfiguration();
        });

        if (this.config == null) {
            return;
        }

        this.guildConfig = new GuildConfig(this.config, logger);
    }

    public @NotNull final GuildConfig getConfig() {
        return this.guildConfig;
    }
}