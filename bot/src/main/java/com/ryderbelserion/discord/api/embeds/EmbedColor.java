package com.ryderbelserion.discord.api.embeds;

import com.ryderbelserion.discord.api.utils.ColorUtils;
import org.jspecify.annotations.NonNull;
import java.awt.*;

public enum EmbedColor {

    DEFAULT("#bff7fd"),
    SUCCESS("#0eeb6a"),
    FAIL("#e0240b"),
    WARNING("#eb6123"),
    EDIT("#5e68ff");

    private final Color color;

    EmbedColor(@NonNull final String code) {
        this.color = ColorUtils.toColor(code);
    }

    public @NonNull final Color getColor() {
        return this.color;
    }
}