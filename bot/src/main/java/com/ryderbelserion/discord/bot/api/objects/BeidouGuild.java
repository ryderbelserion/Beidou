package com.ryderbelserion.discord.bot.api.objects;

import com.ryderbelserion.discord.api.commands.CommandHandler;
import com.ryderbelserion.discord.bot.Beidou;
import com.ryderbelserion.discord.bot.api.managers.CommandManager;
import com.ryderbelserion.discord.bot.api.managers.EmbedManager;
import com.ryderbelserion.discord.bot.configs.ConfigManager;
import com.ryderbelserion.discord.bot.guilds.GuildConfig;
import com.ryderbelserion.discord.bot.guilds.commands.RefreshCommand;
import com.ryderbelserion.fusion.addons.ExtensionManager;
import com.ryderbelserion.fusion.files.FileManager;
import com.ryderbelserion.fusion.files.enums.FileAction;
import com.ryderbelserion.fusion.files.enums.FileType;
import net.dv8tion.jda.api.entities.Guild;
import org.jspecify.annotations.NonNull;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.tinylog.Logger;
import org.tinylog.TaggedLogger;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class BeidouGuild {

    private final Path directory;
    private final String id;

    private final ExtensionManager extensionManager;
    private final ConfigManager configManager;

    private final EmbedManager embedManager;
    private final FileManager fileManager;
    private final CommandHandler handler;
    private final TaggedLogger logger;
    private final Guild guild;

    public BeidouGuild(@NonNull final Beidou instance, @NonNull final Path directory, @NonNull final Guild guild) {
        this.directory = directory;

        this.extensionManager = new ExtensionManager(this.directory.resolve("addons"));

        this.id = guild.getId();

        this.configManager = instance.getConfigManager();
        this.embedManager = instance.getEmbedManager();
        this.fileManager = new FileManager(directory);
        this.handler = instance.getCommandHandler();
        this.logger = Logger.tag("guild");

        this.guild = guild;
    }


    private boolean isExtensionsLoaded = false;
    private CommandManager commandManager;
    private GuildConfig guildConfig;

    public void init(final boolean isReload) {
        this.fileManager.extractFolder("guilds", "guilds", FileType.YAML, directory);

        List.of(
                "config.yml",
                "traffic.yml"
        ).forEach(file -> {
            final List<FileAction> actions = new ArrayList<>();

            final Path path = directory.resolve(file);

            if (Files.exists(path)) {
                actions.add(FileAction.ALREADY_EXTRACTED);
            }

            if (this.fileManager.hasFile(path)) {
                actions.add(FileAction.RELOAD_FILE);
            }

            this.fileManager.addFile(directory.resolve(file), FileType.YAML, builder -> actions.forEach(builder::addAction));
        });

        final Path commandFolder = directory.resolve("commands");
        final Path embedFolder = directory.resolve("embeds");

        if (!Files.exists(commandFolder) || !Files.exists(embedFolder)) {
            this.fileManager.extractFolder("defaults", FileType.YAML, directory);
        }

        final Path folder = directory.resolve("defaults");

        if (!Files.exists(embedFolder)) {
            final Path target = folder.resolve("embeds");

            if (Files.exists(target)) {
                try {
                    Files.move(target, embedFolder, StandardCopyOption.REPLACE_EXISTING);
                } catch (final IOException exception) {
                    exception.printStackTrace();
                }
            }
        }

        if (!Files.exists(commandFolder)) {
            final Path target = folder.resolve("commands");

            if (Files.exists(target)) {
                try {
                    Files.move(target, commandFolder, StandardCopyOption.REPLACE_EXISTING);
                } catch (final IOException exception) {
                    exception.printStackTrace();
                }
            }
        }

        try {
            Files.deleteIfExists(folder);
        } catch (final IOException exception) {
            exception.printStackTrace();
        }

        this.fileManager.addFolder(directory.resolve("commands"), FileType.YAML, consumer -> {
            consumer.addAction(FileAction.ALREADY_EXTRACTED);

            if (isReload) {
                consumer.addAction(FileAction.RELOAD_FILE);
            }
        });

        this.fileManager.addFolder(directory.resolve("embeds"), FileType.YAML, consumer -> {
            consumer.addAction(FileAction.ALREADY_EXTRACTED);

            if (isReload) {
                consumer.addAction(FileAction.RELOAD_FILE);
            }
        });

        final Path addons = directory.resolve("addons");

        try {
            if (!Files.exists(addons)) {
                Files.createDirectory(addons);
            }
        } catch (final IOException exception) {
            exception.printStackTrace();
        }

        final CommentedConfigurationNode config = this.fileManager.getYamlFile(this.directory.resolve("config.yml")).orElseThrow().getConfig();
        final CommentedConfigurationNode traffic = this.fileManager.getYamlFile(this.directory.resolve("traffic.yml")).orElseThrow().getConfig();

        final Path cache = this.directory.resolve("cache.txt");

        if (!Files.exists(cache)) {
            try {
                Files.createFile(cache);
            } catch (final IOException exception) {
                exception.printStackTrace();
            }
        }

        try {
            Files.writeString(cache, this.guild.getName());
        } catch (final IOException exception) {
            exception.printStackTrace();
        }

        this.guildConfig = new GuildConfig(config, traffic, this.embedManager, this.logger);

        this.embedManager.init(this);

        if (this.commandManager == null) {
            this.commandManager = new CommandManager(this);
        }

        this.commandManager.init(List.of(new RefreshCommand(this)));

        if (this.extensionManager != null) {
            if (this.isExtensionsLoaded) {
                this.extensionManager.reload();

                this.logger.warn("Successfully reloaded {} guild addons.", this.extensionManager.getLoadedExtensionCount());

                return;
            }

            this.extensionManager.init(1);

            this.logger.warn("Successfully loaded {} guild addons.", this.extensionManager.getLoadedExtensionCount());

            this.isExtensionsLoaded = true;
        }
    }

    public void purge() {
        if (this.extensionManager != null) {
            this.extensionManager.purge();
        }

        this.commandManager.purge();
        this.embedManager.purge();
        this.fileManager.purge();
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

    public final boolean isOwner(final String id) {
        return this.guild.getId().equals(id);
    }
}