package com.ryderbelserion.discord.bot.api.objects;

import net.dv8tion.jda.api.components.buttons.ButtonStyle;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

public class BeidouButton {

    private final ButtonStyle style;
    private final String value;
    private final String label;
    private final String emoji;
    private final String type;
    private final String id;

    public BeidouButton(@NotNull final CommentedConfigurationNode configuration) {
        final String style = configuration.node("style").getString("primary");

        switch (style) {
            case "secondary" -> this.style = ButtonStyle.SECONDARY;
            case "success" -> this.style = ButtonStyle.SUCCESS;
            case "danger" -> this.style = ButtonStyle.DANGER;
            case "link" -> this.style = ButtonStyle.LINK;
            case "premium" -> this.style = ButtonStyle.PREMIUM;
            default -> this.style = ButtonStyle.PRIMARY;
        }

        this.emoji = configuration.node("action", "emoji").getString("");
        this.value = configuration.node("action", "value").getString("");
        this.type = configuration.node("action", "type").getString("");
        this.label = configuration.node("label").getString("");
        this.id = configuration.node("id").getString("");
    }

    public @NotNull final ButtonStyle getStyle() {
        return this.style;
    }

    public @NotNull final String getValue() {
        return this.value;
    }

    public @NotNull final String getEmoji() {
        return this.emoji;
    }

    public @NotNull final String getLabel() {
        return this.label;
    }

    public @NotNull final String getType() {
        return this.type;
    }

    public @NotNull final String getId() {
        return this.id;
    }
}