package com.ryderbelserion.discord.bot.guilds;

import com.ryderbelserion.discord.bot.Beidou;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class GuildListener extends ListenerAdapter {

    private final Beidou beidou;

    public GuildListener(@NotNull final Beidou beidou) {
        this.beidou = beidou;
    }

    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent event) {
        final Guild guild = event.getGuild();

        this.beidou.onGuildReady(guild);
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        /*final Guild guild = event.getGuild();
        final String id = guild.getId();

        if (!id.equalsIgnoreCase("1142362341095251998")) return;

        this.beidou.getGuildManager().getGuild(id).ifPresent(action -> {
            action.getConfig().test(guild, event.getAuthor());
        });*/
    }
}