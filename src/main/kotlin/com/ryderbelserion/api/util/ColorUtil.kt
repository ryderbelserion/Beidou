package com.ryderbelserion.api.util

import java.awt.Color

object ColorUtil {

    fun String.toColor(): Color {
        return Color(
            Integer.valueOf(substring(1, 3), 16),
            Integer.valueOf(substring(3, 5), 16),
            Integer.valueOf(substring(5, 7), 16)
        )
    }

    /**
     * Converts color to hex string.
     */
    fun String.toHex(color: Color): String {
        return String.format("%02x%02x%02x", color.red, color.green, color.blue)
    }
}