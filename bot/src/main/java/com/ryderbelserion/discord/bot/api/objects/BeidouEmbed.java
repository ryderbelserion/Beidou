package com.ryderbelserion.discord.bot.api.objects;

import com.ryderbelserion.discord.api.embeds.Embed;
import com.ryderbelserion.discord.api.utils.ConfigUtils;
import com.ryderbelserion.discord.api.utils.StringUtils;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.CommentedConfigurationNode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BeidouEmbed {

    private final List<BeidouField> fields = new ArrayList<>();

    private final String description;
    private final boolean isEnabled;
    private final String title;

    private final boolean hasFooter;
    private final String timezone;

    public BeidouEmbed(@NotNull final CommentedConfigurationNode configuration, @NotNull final Map<String, String> placeholders) {
        this.description = StringUtils.replacePlaceholders(StringUtils.toString(ConfigUtils.getStringList(configuration.node("description"))), placeholders);
        this.title = StringUtils.replacePlaceholders(configuration.node("title").getString(""), placeholders);
        this.isEnabled = configuration.node("enabled").getBoolean(false);

        for (final CommentedConfigurationNode node : configuration.node("fields").childrenMap().values()) {
            this.fields.add(new BeidouField(node, placeholders));
        }

        this.hasFooter = configuration.node("footer", "enabled").getBoolean(false);
        this.timezone = configuration.node("footer", "timezone").getString("America/New_York");
    }

    public void sendEmbed(@NotNull final MessageChannelUnion channel, @NotNull final User user) {
        if (!this.isEnabled) return;

        final Embed embed = new Embed();

        if (!this.title.isEmpty()) {
            embed.title(this.title);
        }

        if (!this.description.isEmpty()) {
            embed.description(this.description);
        }

        if (this.hasFooter) {
            embed.footer(user);
        }

        embed.fields(consumer -> {
            for (final BeidouField field : this.fields) {
                consumer.field(field.getTitle(), field.getBody(), field.isInline());
            }
        });

        embed.timestamp(this.timezone);

        channel.sendMessageEmbeds(embed.build()).queue();
    }
}