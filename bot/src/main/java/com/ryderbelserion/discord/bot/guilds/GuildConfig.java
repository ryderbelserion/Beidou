package com.ryderbelserion.discord.bot.guilds;

import com.ryderbelserion.discord.bot.guilds.features.logging.config.MessageConfig;
import com.ryderbelserion.discord.bot.guilds.features.threads.config.ThreadConfig;
import com.ryderbelserion.discord.bot.api.objects.BeidouEmbed;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.spongepowered.configurate.CommentedConfigurationNode;
import java.util.Map;

public class GuildConfig {

    private final MessageConfig messageConfig;
    private final ThreadConfig threadConfig;

    private final CommentedConfigurationNode config;

    public GuildConfig(@NotNull final CommentedConfigurationNode config, @NotNull final Logger logger) {
        this.messageConfig = new MessageConfig(config.node("settings", "channels", "message-logs"), logger);

        this.threadConfig = new ThreadConfig(config.node("settings", "threads", "creation"));

        this.config = config;
    }

    public @NotNull final MessageConfig getMessageConfig() {
        return this.messageConfig;
    }

    public @NotNull final ThreadConfig getThreadConfig() {
        return this.threadConfig;
    }

    public void test(@NotNull final Guild guild, @NotNull final User user) {
        final CommentedConfigurationNode config = this.config.node("settings.embed");

        final TextChannel channel = guild.getTextChannelById("1142581790620524596");

        if (channel != null) {
            new BeidouEmbed(config,
                    Map.of(
                            "{user}", user.getAsMention()
                    ))
            .sendEmbed(channel, user);
        }
    }
}