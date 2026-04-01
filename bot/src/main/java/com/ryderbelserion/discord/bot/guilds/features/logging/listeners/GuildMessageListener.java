package com.ryderbelserion.discord.bot.guilds.features.logging.listeners;

import com.ryderbelserion.discord.bot.Beidou;
import com.ryderbelserion.discord.bot.guilds.GuildManager;
import com.ryderbelserion.discord.bot.guilds.features.logging.config.MessageConfig;
import com.ryderbelserion.fusion.files.FileManager;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.BasicConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

public class GuildMessageListener extends ListenerAdapter {

    private final GuildManager guildManager;

    public GuildMessageListener(@NotNull final Beidou beidou) {
        this.guildManager = beidou.getGuildManager();
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (!event.isFromGuild()) return;

        final User user = event.getAuthor();

        if (user.isSystem()) return;

        final Guild guild = event.getGuild();

        this.guildManager.getGuild(guild.getId()).ifPresent(action -> {
            final FileManager fileManager = action.getFileManager();

            fileManager.getJsonFile(action.getDirectory().resolve("cache.json")).ifPresent(customFile -> {
                final BasicConfigurationNode cache = customFile.getConfiguration();

                final Message message = event.getMessage();

                try {
                    cache.node("id").set(String.class, user.getId());

                    final BasicConfigurationNode id = cache.node("id");

                    id.node("message").set("message").appendListNode();
                } catch (final SerializationException exception) {
                    exception.printStackTrace();
                }
            });

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

        final MessageChannelUnion channel = event.getChannel();
        final String channel_id = channel.getId();

        final String id = event.getMessageId();
        final Message message = event.getMessage();

        final Guild guild = event.getGuild();

        this.guildManager.getGuild(guild.getId()).ifPresent(action -> { // log to channel
            final MessageConfig config = action.getConfig().getMessageConfig();

            config.log(
                    id,
                    "edit",
                    channel,
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
                    id,
                    "delete",
                    channel,
                    guild,
                    user
            );
        });
    }
}