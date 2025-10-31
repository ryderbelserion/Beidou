package com.ryderbelserion.discord.bot.guilds.features.threads.config;

import com.ryderbelserion.discord.api.utils.ConfigUtils;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.unions.GuildMessageChannelUnion;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.CommentedConfigurationNode;
import java.util.List;

public class ThreadConfig {

    private final boolean isEnabled;
    private final List<String> channels;
    private final List<String> users;

    public ThreadConfig(@NotNull final CommentedConfigurationNode configuration) {
        this.isEnabled = configuration.node("enabled").getBoolean(false);
        this.channels = ConfigUtils.getStringList(configuration.node("channels"));
        this.users = ConfigUtils.getStringList(configuration.node("users"));
    }

    public void createThread(@NotNull final GuildMessageChannelUnion channel, @NotNull final User user, @NotNull final Message message) {
        if (!this.isEnabled) return;

        if (!this.channels.contains(channel.getId())) return;

        if (!this.users.isEmpty() && !this.users.contains(user.getId())) return;

        message.createThreadChannel(message.getContentDisplay()).queue(action -> action.addThreadMember(user).queue());
    }
}