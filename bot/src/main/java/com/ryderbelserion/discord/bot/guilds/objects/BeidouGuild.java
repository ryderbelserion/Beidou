package com.ryderbelserion.discord.bot.guilds.objects;

import com.ryderbelserion.discord.bot.guilds.GuildConfig;
import com.ryderbelserion.fusion.addons.AddonManager;
import com.ryderbelserion.fusion.files.FileManager;
import net.dv8tion.jda.api.entities.Guild;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.spongepowered.configurate.BasicConfigurationNode;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import java.nio.file.Path;

public class BeidouGuild {

    private CommentedConfigurationNode config;
    private GuildConfig guildConfig;

    private final AddonManager addonManager;

    public BeidouGuild(@NotNull final FileManager fileManager, @NotNull final Logger logger, @NotNull final Path directory, @NotNull final Guild guild) {
        this.addonManager = new AddonManager(directory);

        init(fileManager, logger, directory, guild);
    }

    public void init(@NotNull final FileManager fileManager, @NotNull final Logger logger, @NotNull final Path directory, @NotNull final Guild guild) {
        fileManager.getYamlFile(directory.resolve("config.yml")).ifPresent(customFile -> this.config = customFile.getConfiguration());

        fileManager.getJsonFile(directory.resolve("cache.json")).ifPresent(customFile -> {
            final BasicConfigurationNode cache = customFile.getConfiguration();

            try {
                cache.node("name").set(String.class, guild.getName());

                customFile.save();
            } catch (final SerializationException exception) {
                exception.printStackTrace();
            }
        });

        if (this.config != null) {
            this.guildConfig = new GuildConfig(this.config, logger);
        }

        if (this.addonManager != null) {
            this.addonManager.load(1);

            this.addonManager.enableAddons();

            logger.warn("Successfully loaded {} addons.", this.addonManager.getAddons().size());
        }
    }

    public @NotNull final AddonManager getAddonManager() {
        return this.addonManager;
    }

    public @NotNull final GuildConfig getConfig() {
        return this.guildConfig;
    }
}