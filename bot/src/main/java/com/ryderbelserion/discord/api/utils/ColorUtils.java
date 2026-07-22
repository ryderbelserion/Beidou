package com.ryderbelserion.discord.api.utils;

import org.jspecify.annotations.NonNull;
import java.awt.*;

public class ColorUtils {

    public static Color toColor(@NonNull final String color) {
        return new Color(
                Integer.parseInt(color.substring(1, 3), 16),
                Integer.parseInt(color.substring(3, 5), 16),
                Integer.parseInt(color.substring(5, 7), 16)
        );
    }

    public static String toHex(@NonNull final Color color) {
        return "%02x%02x%02x".formatted(color.getRed(), color.getGreen(), color.getBlue());
    }
}