package com.ryderbelserion.discord.bot.guilds.listeners;

import com.ryderbelserion.discord.api.commands.CommandEngine;
import com.ryderbelserion.discord.api.commands.CommandHandler;
import com.ryderbelserion.discord.bot.Beidou;
import com.ryderbelserion.discord.bot.api.managers.CommandManager;
import com.ryderbelserion.discord.bot.guilds.GuildManager;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class GuildSlashListener extends ListenerAdapter {

    private final CommandHandler commandHandler;
    private final GuildManager guildManager;

    public GuildSlashListener(@NotNull final Beidou instance) {
        this.commandHandler = instance.getCommandHandler();
        this.guildManager = instance.getGuildManager();
    }

    @Override
    public void onSlashCommandInteraction(@NotNull final SlashCommandInteractionEvent event) {
        final String name = event.getName();

        if (this.commandHandler.hasCommand(name)) { // command is a global/guild command that is static.
            final CommandEngine command = this.commandHandler.getCommand(name);

            command.interact(event);

            return;
        }

        final Guild guild = event.getGuild();

        if (guild != null) { // handles per guild dynamic commands
            final String id = guild.getId();

            this.guildManager.getGuild(id).ifPresent(instance -> {
                final CommandManager commandManager = instance.getCommandManager();

                commandManager.getCommand(name).ifPresent(command -> command.interact(event));
            });
        }
    }

    @Override
    public void onCommandAutoCompleteInteraction(@NotNull final CommandAutoCompleteInteractionEvent event) {
        final String name = event.getName();

        if (this.commandHandler.hasCommand(name)) { // command is a global/guild command that is static.
            final CommandEngine command = this.commandHandler.getCommand(name);

            command.complete(event);

            return;
        }

        final Guild guild = event.getGuild();

        if (guild != null) { // handles per guild dynamic commands
            final String id = guild.getId();

            this.guildManager.getGuild(id).ifPresent(instance -> {
                final CommandManager commandManager = instance.getCommandManager();

                commandManager.getCommand(name).ifPresent(command -> command.complete(event));
            });
        }
    }
}