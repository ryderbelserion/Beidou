package com.ryderbelserion.discord.bot.guilds.features.traffic.config.types;

import com.ryderbelserion.discord.api.utils.ConfigUtils;
import com.ryderbelserion.discord.bot.api.managers.EmbedManager;
import com.ryderbelserion.discord.bot.guilds.features.traffic.config.interfaces.ITrafficConfig;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.CommentedConfigurationNode;
import java.util.List;

public class ExitConfig implements ITrafficConfig {

    private final EmbedManager embedManager;

    private final List<String> channels;
    private final String embed;

    public ExitConfig(
            @NotNull final CommentedConfigurationNode config,
            @NotNull final EmbedManager embedManager
    ) {
        this.embedManager = embedManager;

        this.channels = ConfigUtils.getStringList(config.node("channels"));
        this.embed = config.node("embed").getString("");
    }

    @Override
    public void send(@NotNull final String id, @NotNull final User user, @NotNull final Guild guild) {
        this.embedManager.getEmbed(id, this.embed).ifPresent(embed -> {
            for (final String channel : this.channels) {
                if (channel.isBlank()) continue;

                final TextChannel union = guild.getTextChannelById(channel);

                if (union == null) continue;

                embed.sendEmbed(union, user);
            }
        });
    }
}