package com.ryderbelserion.discord.bot.guilds.listeners;

import com.ryderbelserion.discord.bot.Beidou;
import com.ryderbelserion.discord.bot.guilds.GuildManager;
import com.ryderbelserion.discord.bot.guilds.config.types.MessageConfig;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class MessageListener extends ListenerAdapter {

    private final GuildManager guildManager;

    public MessageListener(@NotNull final Beidou beidou) {
        this.guildManager = beidou.getGuildManager();
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (!event.isFromGuild()) return;

        final User user = event.getAuthor();

        if (user.isSystem()) return;

        final Guild guild = event.getGuild();

        this.guildManager.getGuild(guild.getId()).ifPresent(action -> {
            //final GuildMessage root = action.getGuildMessage();

            //final Message message = event.getMessage();

            //final String content = message.getContentDisplay();
            //final String id = message.getId();

            //root.addMessage(content, message.getAuthor().getId(), id);
        });
    }

    @Override
    public void onMessageUpdate(@NotNull MessageUpdateEvent event) { //todo() store message
        if (!event.isFromGuild()) return;

        final User user = event.getAuthor();

        if (user.isSystem()) return;

        final Channel channel = event.getChannel();
        final String channel_id = channel.getId();

        final String id = event.getMessageId();
        final Message message = event.getMessage();

        final Guild guild = event.getGuild();

        this.guildManager.getGuild(guild.getId()).ifPresent(action -> { // log to channel
            final MessageConfig config = action.getConfig().getMessageConfig();

            config.log(
                    "edit",
                    event.getChannel().asTextChannel(),
                    guild,
                    user
            );
        });
    }

    @Override
    public void onMessageDelete(@NotNull MessageDeleteEvent event) {
        final MessageChannelUnion channel = event.getChannel();
        final String channel_id = channel.getId();

        final String id = event.getMessageId();

        final Guild guild = event.getGuild();

        final User user = event.getJDA().getSelfUser(); //todo() pull user from database.

        this.guildManager.getGuild(guild.getId()).ifPresent(action -> { // log to channel
            final MessageConfig config = action.getConfig().getMessageConfig();

            config.log(
                    "delete",
                    channel.asTextChannel(),
                    guild,
                    user
            );
        });
    }
}