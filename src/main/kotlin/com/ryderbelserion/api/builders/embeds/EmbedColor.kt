package com.ryderbelserion.api.builders.embeds

import com.ryderbelserion.api.util.ColorUtil.toColor
import java.awt.Color

enum class EmbedColor(code: String) {
    DEFAULT("#bff7fd"),
    SUCCESS("#0eeb6a"),
    FAIL("#e0240b"),
    WARNING("#eb6123"),
    EDIT("#5e68ff");

    val color: Color = code.toColor()
}