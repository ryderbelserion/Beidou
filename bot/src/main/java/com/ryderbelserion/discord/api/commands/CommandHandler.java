package com.ryderbelserion.discord.api.commands;

import com.ryderbelserion.discord.api.commands.interfaces.CommandFlow;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import org.jetbrains.annotations.NotNull;

public class CommandHandler implements CommandFlow {

    private JDA jda;

    public void setJda(@NotNull final JDA jda) {
        this.jda = jda;
    }

    @Override
    public void addCommand(@NotNull final CommandEngine engine) {
        this.jda.upsertCommand(engine.getCommandData()).queue();
    }

    @Override
    public void addCommands(@NotNull final CommandEngine... engines) {
        for (final CommandEngine engine : engines) {
            addCommand(engine);
        }
    }

    @Override
    public void addGuildCommand(
            @NotNull final Guild guild,
            @NotNull final CommandEngine engine
    ) {
        guild.upsertCommand(engine.getCommandData()).queue();
    }

    @Override
    public void addGuildCommand(
            @NotNull final Guild guild,
            @NotNull final String command,
            @NotNull final String description
    ) {
        guild.upsertCommand(command, description).queue();
    }

    @Override
    public void addGuildCommands(
            @NotNull final Guild guild,
            @NotNull final CommandEngine... engines
    ) {
        for (final CommandEngine engine : engines) {
            addGuildCommand(guild, engine);
        }
    }

    @Override
    public void purgeGuildCommands(@NotNull final Guild guild) {
        guild.updateCommands().queue();
    }

    @Override
    public void purgeGlobalCommands() {
        this.jda.updateCommands().queue();
    }
}