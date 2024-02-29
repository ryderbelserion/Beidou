package com.ryderbelserion.common.embeds

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.MessageEmbed.Field

class Fields(private val embed: EmbedBuilder) {

    /**
     * Adds a field using Strings.
     *
     * @param title - The title of the embed.
     * @param body - The text for the field description.
     * @param value - Whether the field should be inline.
     */
    fun field(title: String, body: String, value: Boolean = false) {
        this.embed.addField(title, body, value)
    }

    /**
     * Adds a field based on the field object.
     *
     * @param field - The field object containing all the information we need.
     * @param value - Whether the field should be inline.
     */
    fun field(field: Field?, value: Boolean = false) {
        this.embed.addField(field?.name!!, field.value!!, value)
    }

    /**
     * Adds a blank field.
     *
     * @param value - Whether the field should be inline.
     */
    fun empty(value: Boolean = false) {
        this.embed.addBlankField(value)
    }
}