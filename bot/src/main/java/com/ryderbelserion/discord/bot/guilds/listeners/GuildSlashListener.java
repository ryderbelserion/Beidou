package com.ryderbelserion.discord.bot.guilds.listeners;

import com.ryderbelserion.discord.bot.Beidou;
import com.ryderbelserion.discord.bot.api.managers.CommandManager;
import com.ryderbelserion.discord.bot.guilds.GuildManager;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class GuildSlashListener extends ListenerAdapter {

    private final GuildManager guildManager;

    public GuildSlashListener(@NotNull final Beidou instance) {
        this.guildManager = instance.getGuildManager();
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        final Guild guild = event.getGuild();

        if (guild != null) {
            final String id = guild.getId();

            this.guildManager.getGuild(id).ifPresent(instance -> {
                final CommandManager commandManager = instance.getCommandManager();

                commandManager.getCommand(event.getName()).ifPresent(command -> command.interact(event));
            });
        }
    }
}