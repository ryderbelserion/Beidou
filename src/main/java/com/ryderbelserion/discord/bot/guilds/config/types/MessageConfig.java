package com.ryderbelserion.discord.bot.guilds.config.types;

import com.ryderbelserion.discord.api.embeds.Embed;
import com.ryderbelserion.discord.api.utils.ConfigUtils;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.spongepowered.configurate.CommentedConfigurationNode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageConfig {

    private final Map<String, List<String>> channels = new HashMap<>();

    private final List<String> ignoredChannels;
    private final List<String> ignoredRoles;
    private final List<String> ignoredUsers;

    private final String defaultChannel;

    private final Logger logger;

    public MessageConfig(@NotNull final CommentedConfigurationNode config, @NotNull final Logger logger) {
        this.defaultChannel = config.node("default").getString("");

        this.channels.put("delete", ConfigUtils.getStringList(config.node("types", "deletion")));
        this.channels.put("edit", ConfigUtils.getStringList(config.node("types", "edits")));

        this.ignoredChannels = ConfigUtils.getStringList(config.node("channels"));
        this.ignoredRoles = ConfigUtils.getStringList(config.node("roles"));
        this.ignoredUsers = ConfigUtils.getStringList(config.node("users"));

        this.logger = logger;
    }

    public void log(@NotNull final String type, @NotNull final TextChannel channel, @NotNull final Guild guild, @NotNull final User user) {
        final List<String> channels = this.channels.get(type);

        final boolean isEmpty = this.defaultChannel.isEmpty();

        final String channel_id = channel.getId();

        final String user_id = user.getId();

        if (this.ignoredUsers.contains(user_id)) {
            this.logger.warn("The user {} bypasses message editing/deletion, because they have are set to be ignored.", user_id);

            return;
        }

        if (this.ignoredChannels.contains(channel_id)) {
            return;
        }

        final Member member = guild.getMember(user);

        boolean hasRole = false;

        if (member != null) {
            final List<Role> roles = member.getRoles();

            for (final Role role : roles) {
                final String role_id = role.getId();

                if (this.ignoredRoles.contains(role_id)) {
                    hasRole = true;

                    break;
                }
            }
        }

        if (hasRole) { // has ignored role, do not log/edit
            this.logger.warn("The user {} bypasses message editing/deletion, because they have a role that is ignored", user_id);

            return;
        }

        switch (type) {
            case "delete" -> {
                final Embed embed = new Embed().description("A message was deleted!");

                boolean hasLogged = false;

                for (final String id : channels) {
                    final TextChannel textChannel = guild.getTextChannelById(id);

                    if (textChannel != null) {
                        textChannel.sendMessageEmbeds(embed.build()).queue();

                        hasLogged = true;

                        continue;
                    }

                    this.logger.warn("The channel id {} when logging deleted messages for multiple channels does not belong to an existing channel!", id);
                }

                if (hasLogged) return;

                if (!isEmpty) {
                    final TextChannel textChannel = guild.getTextChannelById(this.defaultChannel);

                    if (textChannel != null) {
                        textChannel.sendMessageEmbeds(embed.build()).queue();

                        return;
                    }

                    this.logger.warn("The channel id {} when logging deleted messages does not belong to an existing channel!", this.defaultChannel);
                }
            }

            case "edit" -> {
                final Embed embed = new Embed().description("A message was edited!");

                boolean hasLogged = false;

                for (final String id : channels) {
                    final TextChannel textChannel = guild.getTextChannelById(id);

                    if (textChannel != null) {
                        textChannel.sendMessageEmbeds(embed.build()).queue();

                        hasLogged = true;

                        continue;
                    }

                    this.logger.warn("The channel id {} when logging edited messages for multiple channels does not belong to an existing channel!", id);
                }

                if (hasLogged) return;

                if (!isEmpty) {
                    final TextChannel textChannel = guild.getTextChannelById(this.defaultChannel);

                    if (textChannel != null) {
                        textChannel.sendMessageEmbeds(embed.build()).queue();

                        return;
                    }

                    this.logger.warn("The channel id {} when logging edited messages does not belong to an existing channel!", this.defaultChannel);
                }
            }
        }
    }
}