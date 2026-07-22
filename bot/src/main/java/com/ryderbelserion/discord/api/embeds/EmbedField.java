package com.ryderbelserion.discord.api.embeds;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.jspecify.annotations.NonNull;

public class EmbedField {

    private final EmbedBuilder builder;

    public EmbedField(@NonNull final EmbedBuilder builder) {
        this.builder = builder;
    }

    public void field(@NonNull final String title, @NonNull final String body, final boolean inline) {
        this.builder.addField(title, body, inline);
    }

    public void field(final MessageEmbed.@NonNull Field field, final boolean inline) {
        final String name = field.getName();
        final String value = field.getValue();

        field(name != null ? name : "", value != null ? value : "", inline);
    }

    public void field(@NonNull final String title, @NonNull final String body) {
        field(title, body, false);
    }

    public void field(final MessageEmbed.@NonNull Field field) {
        final String name = field.getName();
        final String value = field.getValue();

        field(name != null ? name : "", value != null ? value : "", field.isInline());
    }

    public void empty(final boolean inline) {
        this.builder.addBlankField(inline);
    }
}