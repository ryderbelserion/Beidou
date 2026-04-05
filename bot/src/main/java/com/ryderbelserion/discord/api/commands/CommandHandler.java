package com.ryderbelserion.discord.api.commands;

import com.ryderbelserion.discord.api.commands.interfaces.CommandFlow;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.jetbrains.annotations.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CommandHandler implements CommandFlow {

    private final Map<String, CommandEngine> commands = new HashMap<>();

    private JDA jda;

    public void setJda(@NotNull final JDA jda) {
        this.jda = jda;
    }

    @Override
    public void addCommands(@NotNull final List<CommandEngine> commands) {
        this.jda.updateCommands().addCommands(commands.stream()
                .map(CommandEngine::getCommandData)
                .collect(Collectors.toSet())).queue();

        for (final CommandEngine engine : commands) { // add to cache
            this.commands.put(engine.getName(), engine);
        }
    }

    @Override
    public void addStaticGuildCommands(@NotNull final Guild guild, @NotNull final List<CommandEngine> commands) { // statically generated guild commands, i.e. on startup only, hard coded.
        addGuildCommands(guild, commands.stream().map(CommandEngine::getCommandData).toList());

        for (final CommandEngine engine : commands) { // add to cache
            this.commands.put(engine.getName(), engine);
        }
    }

    @Override
    public void addGuildCommands(@NotNull final Guild guild, @NotNull final List<CommandData> commands) { // dynamically generated guild commands, i.e. yml files
        guild.updateCommands().addCommands(commands).queue();
    }

    @Override
    public void purgeGuildCommands(@NotNull final Guild guild) {
        guild.updateCommands().queue();
    }

    @Override
    public void purgeCommands() {
        this.jda.updateCommands().queue();
    }

    public @NotNull final CommandEngine getCommand(@NotNull final String name) {
        return this.commands.get(name);
    }

    public final boolean hasCommand(@NotNull final String name) {
        return this.commands.containsKey(name);
    }
}