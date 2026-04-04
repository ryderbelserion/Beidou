package com.ryderbelserion.discord.bot.guilds.features.traffic;

import com.ryderbelserion.discord.bot.Beidou;
import com.ryderbelserion.discord.bot.guilds.GuildManager;
import com.ryderbelserion.discord.bot.guilds.features.traffic.config.TrafficConfig;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class TrafficListener extends ListenerAdapter {

    private final GuildManager guildManager;

    public TrafficListener(@NotNull final Beidou beidou) {
        this.guildManager = beidou.getGuildManager();
    }

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        final Guild guild = event.getGuild();

        final String id = guild.getId();

        this.guildManager.getGuild(id).ifPresent(action -> {
            final TrafficConfig config = action.getConfig().getTrafficConfig();

            config.send(id, "entry", event.getUser(), guild);
        });
    }

    @Override
    public void onGuildMemberRemove(@NotNull GuildMemberRemoveEvent event) {
        final Guild guild = event.getGuild();

        final String id = guild.getId();

        this.guildManager.getGuild(id).ifPresent(action -> {
            final TrafficConfig config = action.getConfig().getTrafficConfig();

            config.send(id, "exit", event.getUser(), guild);
        });
    }
}