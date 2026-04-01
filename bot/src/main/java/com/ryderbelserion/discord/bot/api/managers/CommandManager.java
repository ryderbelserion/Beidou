package com.ryderbelserion.discord.bot.api.managers;

import com.ryderbelserion.discord.api.commands.CommandHandler;
import com.ryderbelserion.discord.bot.api.objects.BeidouGuild;
import com.ryderbelserion.discord.bot.api.objects.commands.BeidouCommand;
import com.ryderbelserion.fusion.files.FileManager;
import com.ryderbelserion.fusion.files.types.configurate.YamlCustomFile;
import net.dv8tion.jda.api.entities.Guild;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.CommentedConfigurationNode;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class CommandManager {

    private final Map<String, BeidouCommand> commands = new HashMap<>();

    private final FileManager fileManager;
    private final EmbedManager embedManager;
    private final CommandHandler handler;
    private final Path path;
    private final String id;
    private final Guild guild;

    public CommandManager(@NotNull final BeidouGuild guild) {
        this.fileManager = guild.getFileManager();
        this.embedManager = guild.getEmbedManager();
        this.handler = guild.getHandler();
        this.path = guild.getDirectory();
        this.guild = guild.getGuild();
        this.id = guild.getId();
    }

    public void init() {
        this.handler.purgeGuildCommands(this.guild);

        this.commands.clear();

        for (final Path path : this.fileManager.getFilesByPath(this.path.resolve("commands"), ".yml", 1)) {
            final Optional<YamlCustomFile> optional = this.fileManager.getYamlFile(path);

            if (optional.isEmpty()) continue;

            final CommentedConfigurationNode configuration = optional.get().getConfiguration();

            final BeidouCommand command = new BeidouCommand(this.id, configuration, this.embedManager);

            if (!command.isEnabled()) continue;

            final String name = command.getCommand();
            final String desc = command.getDescription();

            this.commands.put(name, command);

            this.handler.addGuildCommand(this.guild, name, desc);
        }
    }

    public @NotNull final Optional<BeidouCommand> getCommand(@NotNull final String key) {
        return Optional.ofNullable(this.commands.getOrDefault(key, null));
    }

    public @NotNull final Map<String, BeidouCommand> getCommands() {
        return Collections.unmodifiableMap(this.commands);
    }
}