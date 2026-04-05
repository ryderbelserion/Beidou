package com.ryderbelserion.discord.bot.api.managers;

import com.ryderbelserion.discord.api.commands.CommandHandler;
import com.ryderbelserion.discord.bot.api.objects.BeidouGuild;
import com.ryderbelserion.discord.bot.api.objects.commands.BeidouCommand;
import com.ryderbelserion.fusion.files.FileManager;
import com.ryderbelserion.fusion.files.types.configurate.YamlCustomFile;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.CommentedConfigurationNode;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class CommandManager {

    private final Map<String, BeidouCommand> commands = new HashMap<>();

    private final CommandHandler commandHandler;
    private final EmbedManager embedManager;
    private final FileManager fileManager;
    private final BeidouGuild instance;
    private final Path path;
    private final String id;

    public CommandManager(@NotNull final BeidouGuild guild) {
        this.commandHandler = guild.getCommandHandler();
        this.embedManager = guild.getEmbedManager();
        this.fileManager = guild.getFileManager();
        this.path = guild.getDirectory();
        this.id = guild.getId();

        this.instance = guild;
    }

    public void init() {
        this.commands.clear();

        final int depth = this.instance.getConfigManager().getConfig().getFileConfig().getRecursionDepth();

        for (final Path path : this.fileManager.getFilesByPath(this.path.resolve("commands"), ".yml", depth)) {
            final Optional<YamlCustomFile> optional = this.fileManager.getYamlFile(path);

            if (optional.isEmpty()) continue;

            final CommentedConfigurationNode configuration = optional.get().getConfiguration();

            final BeidouCommand command = new BeidouCommand(this.id, configuration, this.embedManager);

            if (!command.isEnabled()) continue;

            this.commands.put(command.getName(), command);
        }

        this.commandHandler.addGuildCommands(this.instance.getGuild(), this.commands.values().stream().map(BeidouCommand::getCommandData).toList());
    }

    public @NotNull final Optional<BeidouCommand> getCommand(@NotNull final String key) {
        return Optional.ofNullable(this.commands.getOrDefault(key, null));
    }

    public @NotNull final Map<String, BeidouCommand> getCommands() {
        return Collections.unmodifiableMap(this.commands);
    }
}