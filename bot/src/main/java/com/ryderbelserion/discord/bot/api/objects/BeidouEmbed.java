package com.ryderbelserion.discord.bot.api.objects;

import com.ryderbelserion.discord.api.embeds.Embed;
import com.ryderbelserion.discord.api.utils.ConfigUtils;
import com.ryderbelserion.discord.api.utils.StringUtils;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.CommentedConfigurationNode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class BeidouEmbed {

    private final List<BeidouField> fields = new ArrayList<>();

    private final String description;
    private final boolean isEnabled;
    private final boolean isSilent;
    private final String title;

    private final boolean hasFooter;
    private final String timezone;
    private final String color;

    public BeidouEmbed(@NotNull final CommentedConfigurationNode configuration, @NotNull final Map<String, String> placeholders) {
        this.description = StringUtils.replacePlaceholders(StringUtils.toString(ConfigUtils.getStringList(configuration.node("description"))), placeholders);
        this.title = StringUtils.replacePlaceholders(configuration.node("title").getString(""), placeholders);
        this.isEnabled = configuration.node("enabled").getBoolean(false);
        this.isSilent = configuration.node("silent").getBoolean(false);

        for (final CommentedConfigurationNode node : configuration.node("fields").childrenMap().values()) {
            this.fields.add(new BeidouField(node, placeholders));
        }

        this.hasFooter = configuration.node("footer", "enabled").getBoolean(false);
        this.timezone = configuration.node("footer", "timezone").getString("America/New_York");

        this.color = configuration.node("color").getString("#e91e63");
    }

    public @Nullable final MessageEmbed buildEmbed(@NotNull final Consumer<Embed> consumer) {
        if (!this.isEnabled) return null;

        final Embed embed = new Embed();

        if (!this.title.isEmpty()) {
            embed.title(this.title);
        }

        if (!this.description.isEmpty()) {
            embed.description(this.description);
        }

        embed.fields(action -> {
            for (final BeidouField field : this.fields) {
                action.field(field.getTitle(), field.getBody(), field.isInline());
            }
        });

        embed.timestamp(this.timezone);

        embed.color(this.color);

        consumer.accept(embed);

        return embed.build();
    }

    public void sendEmbed(@NotNull final SlashCommandInteractionEvent event, @NotNull final User user, @NotNull final List<BeidouEmbed> embeds) { // slash interactions
        final MessageEmbed message = buildEmbed(consumer -> {
            if (this.hasFooter) {
                consumer.footer(user);
            }
        });

        if (message == null) {
            return;
        }

        final List<MessageEmbed> values = new ArrayList<>();

        if (!embeds.isEmpty()) {
            for (final BeidouEmbed embed : embeds) {
                final MessageEmbed key = embed.buildEmbed(consumer -> {
                    if (embed.hasFooter()) {
                        consumer.footer(user);
                    }
                });

                if (key == null) continue;

                values.add(key);
            }
        }

        event.replyEmbeds(message).addEmbeds(values).setEphemeral(this.isSilent).queue();
    }

    public void sendEmbed(@NotNull final SlashCommandInteractionEvent event, @NotNull final User user) {
        sendEmbed(event, user, List.of());
    }

    public void sendEmbed(@NotNull final MessageChannelUnion channel, @NotNull final User user, @NotNull final List<BeidouEmbed> embeds) { // channel replies
        final MessageEmbed message = buildEmbed(consumer -> {
            if (this.hasFooter) {
                consumer.footer(user);
            }
        });

        if (message == null) {
            return;
        }

        final List<MessageEmbed> values = new ArrayList<>();

        if (!embeds.isEmpty()) {
            for (final BeidouEmbed embed : embeds) {
                final MessageEmbed key = embed.buildEmbed(consumer -> {
                    if (embed.hasFooter()) {
                        consumer.footer(user);
                    }
                });

                if (key == null) continue;

                values.add(key);
            }
        }

        channel.sendMessageEmbeds(message).addEmbeds(values).queue();
    }

    public void sendEmbed(@NotNull final MessageChannelUnion channel, @NotNull final User user) {
        sendEmbed(channel, user, List.of());
    }

    public final boolean hasFooter() {
        return this.hasFooter;
    }
}