package com.ryderbelserion.discord.bot.guilds.features.threads.config;

import com.ryderbelserion.discord.api.utils.ConfigUtils;
import com.ryderbelserion.discord.api.utils.StringUtils;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.unions.GuildMessageChannelUnion;
import org.jspecify.annotations.NonNull;
import org.spongepowered.configurate.CommentedConfigurationNode;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ThreadConfig {

    private final Map<String, List<String>> channels = new HashMap<>();

    private final boolean isEnabled;
    private final String title;

    public ThreadConfig(@NonNull final CommentedConfigurationNode configuration) {
        this.isEnabled = configuration.node("enabled").getBoolean(false);
        this.title = configuration.node("title").getString("{date}");

        final CommentedConfigurationNode keys = configuration.node("channels");

        keys.childrenMap().forEach((key, node) -> this.channels.putIfAbsent(key.toString(), ConfigUtils.getStringList(node)));
    }

    public void createThread(@NonNull final GuildMessageChannelUnion channel, @NonNull final User user, @NonNull final Message message) {
        if (!this.isEnabled) return;

        final String id = channel.getId();

        if (!this.channels.containsKey(id)) return;

        final List<String> users = this.channels.getOrDefault(id, List.of());

        if (!users.isEmpty() && !users.contains(user.getId())) return;

        final String display = message.getContentDisplay();

        message.createThreadChannel(display.isBlank() ? StringUtils.replacePlaceholders(this.title, Map.of(
                "{date}",
                ZonedDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a"))
        )) : display.substring(0, Math.min(100, display.length()))).queue(action -> {
            if (!message.isWebhookMessage()) {
                action.addThreadMember(user).queue();
            }
        });
    }
}