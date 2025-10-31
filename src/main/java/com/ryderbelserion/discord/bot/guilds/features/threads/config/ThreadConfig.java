package com.ryderbelserion.discord.bot.guilds.features.threads.config;

import com.ryderbelserion.discord.api.utils.ConfigUtils;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.unions.GuildMessageChannelUnion;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.CommentedConfigurationNode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ThreadConfig {

    private final Map<String, List<String>> channels = new HashMap<>();

    private final boolean isEnabled;

    public ThreadConfig(@NotNull final CommentedConfigurationNode configuration) {
        this.isEnabled = configuration.node("enabled").getBoolean(false);

        final CommentedConfigurationNode keys = configuration.node("channels");

        keys.childrenMap().forEach((key, node) -> this.channels.putIfAbsent(key.toString(), ConfigUtils.getStringList(node)));
    }

    public void createThread(@NotNull final GuildMessageChannelUnion channel, @NotNull final User user, @NotNull final Message message) {
        if (!this.isEnabled) return;

        final String id = channel.getId();

        if (!this.channels.containsKey(id)) return;

        final List<String> users = this.channels.getOrDefault(id, List.of());

        if (!users.isEmpty() && !users.contains(user.getId())) return;

        message.createThreadChannel(message.getContentDisplay()).queue(action -> action.addThreadMember(user).queue());
    }
}