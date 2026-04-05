package com.ryderbelserion.discord.bot.api.objects;

import com.ryderbelserion.discord.api.commands.CommandHandler;
import com.ryderbelserion.discord.bot.Beidou;
import com.ryderbelserion.discord.bot.api.managers.CommandManager;
import com.ryderbelserion.discord.bot.api.managers.EmbedManager;
import com.ryderbelserion.discord.bot.configs.ConfigManager;
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
    private CommentedConfigurationNode traffic;
    private GuildConfig guildConfig;
    private final Path directory;
    private final String id;

    private final ConfigManager configManager;
    private final AddonManager addonManager;
    private final EmbedManager embedManager;
    private final FileManager fileManager;
    private final CommandHandler handler;
    private final Logger logger;
    private final Guild guild;

    public BeidouGuild(@NotNull final Beidou instance, @NotNull final Path directory, @NotNull final Guild guild) {
        this.addonManager = new AddonManager(this.directory = directory);

        this.id = guild.getId();

        this.configManager = instance.getConfigManager();
        this.embedManager = instance.getEmbedManager();
        this.fileManager = instance.getFileManager();
        this.handler = instance.getCommandHandler();
        this.logger = instance.getLogger();

        this.guild = guild;
    }

    private CommandManager commandManager;

    public void init() {
        this.fileManager.getYamlFile(this.directory.resolve("config.yml")).ifPresent(customFile -> this.config = customFile.getConfiguration());

        this.fileManager.getYamlFile(this.directory.resolve("traffic.yml")).ifPresent(customFile -> this.traffic = customFile.getConfiguration());

        this.fileManager.getJsonFile(this.directory.resolve("cache.json")).ifPresent(customFile -> {
            final BasicConfigurationNode cache = customFile.getConfiguration();

            try {
                cache.node("name").set(String.class, this.guild.getName());

                customFile.save();
            } catch (final SerializationException exception) {
                exception.printStackTrace();
            }
        });

        if (this.config != null) {
            this.guildConfig = new GuildConfig(this.config, this.traffic, this.embedManager, this.logger);
        }

        this.embedManager.init(this);

        if (this.commandManager == null) {
            this.commandManager = new CommandManager(this);
        }

        this.commandManager.init();

        if (this.addonManager != null) {
            this.addonManager.load(1);

            this.addonManager.enableAddons();

            this.logger.warn("Successfully loaded {} addons.", this.addonManager.getAddons().size());
        }
    }

    public @NotNull final CommandHandler getCommandHandler() {
        return this.handler;
    }

    public @NotNull final CommandManager getCommandManager() {
        return this.commandManager;
    }

    public @NotNull final ConfigManager getConfigManager() {
        return this.configManager;
    }

    public @NotNull final EmbedManager getEmbedManager() {
        return this.embedManager;
    }

    public @NotNull final AddonManager getAddonManager() {
        return this.addonManager;
    }

    public @NotNull final FileManager getFileManager() {
        return this.fileManager;
    }

    public @NotNull final GuildConfig getConfig() {
        return this.guildConfig;
    }

    public @NotNull final Path getDirectory() {
        return this.directory;
    }

    public @NotNull final Guild getGuild() {
        return this.guild;
    }

    public @NotNull final String getId() {
        return this.id;
    }
}