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
import java.util.HashMap;
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
    private final boolean hasTimeStamp;

    public BeidouEmbed(@NotNull final CommentedConfigurationNode configuration) {
        this.description = StringUtils.toString(ConfigUtils.getStringList(configuration.node("description")));
        this.title = configuration.node("title").getString("");
        this.isEnabled = configuration.node("enabled").getBoolean(false);
        this.isSilent = configuration.node("silent").getBoolean(false);

        for (final CommentedConfigurationNode node : configuration.node("fields").childrenMap().values()) {
            this.fields.add(new BeidouField(node));
        }

        this.hasFooter = configuration.node("footer", "enabled").getBoolean(false);
        this.timezone = configuration.node("footer", "timezone").getString("America/New_York");
        this.hasTimeStamp = configuration.node("footer", "timestamp").getBoolean(false);

        this.color = configuration.node("color").getString("#e91e63");
    }

    public @Nullable final MessageEmbed buildEmbed(@NotNull final Map<String, String> placeholders, @NotNull final Consumer<Embed> consumer) {
        if (!this.isEnabled) return null;

        final Embed embed = new Embed();

        if (!this.title.isEmpty()) {
            embed.title(StringUtils.replacePlaceholders(this.title, placeholders));
        }

        if (!this.description.isEmpty()) {
            embed.description(StringUtils.replacePlaceholders(this.description, placeholders));
        }

        embed.fields(action -> {
            for (final BeidouField field : this.fields) {
                action.field(StringUtils.replacePlaceholders(field.getTitle(), placeholders), StringUtils.replacePlaceholders(field.getBody(), placeholders), field.isInline());
            }
        });

        embed.timestamp(this.timezone);

        embed.color(this.color);

        consumer.accept(embed);

        return embed.build();
    }

    public void sendEmbed(
            @NotNull final SlashCommandInteractionEvent event,
            @NotNull final User user,
            @NotNull final Map<String, String> values,
            @NotNull final List<BeidouEmbed> embeds
    ) { // slash interactions
        final Map<String, String> placeholders = new HashMap<>(values);

        placeholders.put("{usertag}", user.getAsTag());
        placeholders.put("{username}", user.getName());
        placeholders.put("{userid}", user.getId());

        placeholders.put("{usermention}", user.getAsMention());

        final MessageEmbed message = buildEmbed(placeholders, consumer -> {
            if (this.hasFooter) {
                consumer.footer(user);
            }

            if (this.hasTimeStamp) {
                consumer.timestamp(this.timezone);
            }
        });

        if (message == null) {
            return;
        }

        final List<MessageEmbed> keys = new ArrayList<>();

        if (!embeds.isEmpty()) {
            for (final BeidouEmbed embed : embeds) {
                final MessageEmbed key = embed.buildEmbed(placeholders, consumer -> {
                    if (embed.hasFooter()) {
                        consumer.footer(user);
                    }
                });

                if (key == null) continue;

                keys.add(key);
            }
        }

        event.replyEmbeds(message).addEmbeds(keys).setEphemeral(this.isSilent).queue();
    }

    public void sendEmbed(
            @NotNull final SlashCommandInteractionEvent event,
            @NotNull final Map<String, String> values,
            @NotNull final User user
    ) {
        sendEmbed(event, user, values, List.of());
    }

    public void sendEmbed(@NotNull final SlashCommandInteractionEvent event, @NotNull final User user) {
        sendEmbed(event, user, Map.of(), List.of());
    }

    public void sendEmbed(
            @NotNull final MessageChannelUnion channel,
            @NotNull final User user,
            @NotNull final Map<String, String> values,
            @NotNull final List<BeidouEmbed> embeds
    ) { // channel replies
        final Map<String, String> placeholders = new HashMap<>(values);

        placeholders.put("{usertag}", user.getAsTag());
        placeholders.put("{username}", user.getName());
        placeholders.put("{userid}", user.getId());

        placeholders.put("{usermention}", user.getAsMention());

        final MessageEmbed message = buildEmbed(placeholders, consumer -> {
            if (this.hasFooter) {
                consumer.footer(user);
            }

            if (this.hasTimeStamp) {
                consumer.timestamp(this.timezone);
            }
        });

        if (message == null) {
            return;
        }

        final List<MessageEmbed> keys = new ArrayList<>();

        if (!embeds.isEmpty()) {
            for (final BeidouEmbed embed : embeds) {
                final MessageEmbed key = embed.buildEmbed(placeholders, consumer -> {
                    if (embed.hasFooter()) {
                        consumer.footer(user);
                    }

                    if (embed.hasTimeStamp()) {
                        consumer.timestamp(embed.getTimezone());
                    }
                });

                if (key == null) continue;

                keys.add(key);
            }
        }

        channel.sendMessageEmbeds(message).addEmbeds(keys).queue();
    }

    public void sendEmbed(
            @NotNull final MessageChannelUnion channel,
            @NotNull final Map<String, String> values,
            @NotNull final User user
    ) {
        sendEmbed(channel, user, values, List.of());
    }

    public void sendEmbed(@NotNull final MessageChannelUnion channel, @NotNull final User user) {
        sendEmbed(channel, user, Map.of(), List.of());
    }

    public @NotNull final String getTimezone() {
        return this.timezone;
    }

    public final boolean hasFooter() {
        return this.hasFooter;
    }

    public final boolean hasTimeStamp() {
        return this.hasTimeStamp;
    }
}