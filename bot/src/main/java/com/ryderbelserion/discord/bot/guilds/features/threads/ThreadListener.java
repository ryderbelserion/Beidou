package com.ryderbelserion.discord.bot.guilds.features.threads;

import com.ryderbelserion.discord.bot.Beidou;
import com.ryderbelserion.discord.bot.guilds.GuildManager;
import com.ryderbelserion.discord.bot.guilds.features.threads.config.ThreadConfig;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.unions.GuildMessageChannelUnion;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class ThreadListener extends ListenerAdapter {

    private final GuildManager guildManager;

    public ThreadListener(@NotNull final Beidou beidou) {
        this.guildManager = beidou.getGuildManager();
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (!event.isFromGuild()) return;

        final User user = event.getAuthor();

        if (user.isSystem()) return;

        final Guild guild = event.getGuild();

        final GuildMessageChannelUnion channel = event.getGuildChannel();

        this.guildManager.getGuild(guild.getId()).ifPresent(action -> {
            final ThreadConfig config = action.getConfig().getThreadConfig();

            config.createThread(channel, user, event.getMessage());
        });
    }
}