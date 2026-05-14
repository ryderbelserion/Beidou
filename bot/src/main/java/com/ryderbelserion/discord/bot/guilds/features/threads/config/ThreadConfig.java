package com.ryderbelserion.discord.bot.guilds.features.threads.config;

import com.ryderbelserion.discord.api.utils.ConfigUtils;
import com.ryderbelserion.discord.api.utils.StringUtils;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.unions.GuildMessageChannelUnion;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.CommentedConfigurationNode;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

public class ThreadConfig {

    private final Map<String, List<String>> channels = new HashMap<>();

    private final boolean isEnabled;
    private final String title;

    public ThreadConfig(@NotNull final CommentedConfigurationNode configuration) {
        this.isEnabled = configuration.node("enabled").getBoolean(false);
        this.title = StringUtils.replacePlaceholders(configuration.node("title").getString("{date}"), Map.of(
                "{date}",
                ZonedDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a"))
        ));

        final CommentedConfigurationNode keys = configuration.node("channels");

        keys.childrenMap().forEach((key, node) -> this.channels.putIfAbsent(key.toString(), ConfigUtils.getStringList(node)));
    }

    public void createThread(@NotNull final GuildMessageChannelUnion channel, @NotNull final User user, @NotNull final Message message) {
        if (!this.isEnabled) return;

        final String id = channel.getId();

        if (!this.channels.containsKey(id)) return;

        final List<String> users = this.channels.getOrDefault(id, List.of());

        if (!users.isEmpty() && !users.contains(user.getId())) return;

        final String display = message.getContentDisplay();

        message.createThreadChannel(display.isBlank() ? this.title : display.substring(0, Math.min(100, display.length()))).queue(action -> {
            if (!message.isWebhookMessage()) {
                action.addThreadMember(user).queue();
            }
        });
    }
}