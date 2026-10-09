package com.ryderbelserion.discord.bot.api.objects;

import com.ryderbelserion.discord.api.commands.CommandHandler;
import com.ryderbelserion.discord.bot.Beidou;
import com.ryderbelserion.discord.bot.api.managers.CommandManager;
import com.ryderbelserion.discord.bot.api.managers.EmbedManager;
import com.ryderbelserion.discord.bot.configs.ConfigManager;
import com.ryderbelserion.discord.bot.guilds.GuildConfig;
import com.ryderbelserion.fusion.addons.ExtensionManager;
import com.ryderbelserion.fusion.files.FileManager;
import net.dv8tion.jda.api.entities.Guild;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.spongepowered.configurate.BasicConfigurationNode;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import java.nio.file.Path;

public class BeidouGuild {

    private final Path directory;
    private final String id;

    private final ExtensionManager extensionManager;
    private final ConfigManager configManager;

    private final EmbedManager embedManager;
    private final FileManager fileManager;
    private final CommandHandler handler;
    private final Logger logger;
    private final Guild guild;

    public BeidouGuild(@NonNull final Beidou instance, @NonNull final Path directory, @NonNull final Guild guild) {
        this.extensionManager = new ExtensionManager(this.directory = directory);

        this.id = guild.getId();

        this.configManager = instance.getConfigManager();
        this.embedManager = instance.getEmbedManager();
        this.fileManager = instance.getFileManager();
        this.handler = instance.getCommandHandler();
        this.logger = instance.getLogger();

        this.guild = guild;
    }


    private CommandManager commandManager;
    private GuildConfig guildConfig;

    public void init() {
        final CommentedConfigurationNode config = this.fileManager.getYamlFile(this.directory.resolve("config.yml")).orElseThrow().getConfiguration();
        final CommentedConfigurationNode traffic = this.fileManager.getYamlFile(this.directory.resolve("traffic.yml")).orElseThrow().getConfiguration();

        this.fileManager.getJsonFile(this.directory.resolve("cache.json")).ifPresent(customFile -> {
            final BasicConfigurationNode cache = customFile.getConfiguration();

            try {
                cache.node("name").set(String.class, this.guild.getName());

                customFile.save();
            } catch (final SerializationException exception) {
                exception.printStackTrace();
            }
        });

        this.guildConfig = new GuildConfig(config, traffic, this.embedManager, this.logger);

        this.embedManager.init(this);

        if (this.commandManager == null) {
            this.commandManager = new CommandManager(this);
        }

        this.commandManager.init();

        if (this.extensionManager != null) {
            this.extensionManager.init(1);

            this.logger.warn("Successfully loaded {} addons.", this.extensionManager.getLoadedExtensionCount());
        }
    }

    public @NonNull final ExtensionManager getExtensionManager() {
        return this.extensionManager;
    }

    public @NonNull final CommandHandler getCommandHandler() {
        return this.handler;
    }

    public @NonNull final CommandManager getCommandManager() {
        return this.commandManager;
    }

    public @NonNull final ConfigManager getConfigManager() {
        return this.configManager;
    }

    public @NonNull final EmbedManager getEmbedManager() {
        return this.embedManager;
    }

    public @NonNull final FileManager getFileManager() {
        return this.fileManager;
    }

    public @NonNull final GuildConfig getConfig() {
        return this.guildConfig;
    }

    public @NonNull final Path getDirectory() {
        return this.directory;
    }

    public @NonNull final Guild getGuild() {
        return this.guild;
    }

    public @NonNull final String getId() {
        return this.id;
    }
}