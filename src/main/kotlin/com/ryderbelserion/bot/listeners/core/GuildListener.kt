package com.ryderbelserion.bot.listeners.core

import com.ryderbelserion.bot.Beidou
import net.dv8tion.jda.api.events.guild.GuildJoinEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class GuildListener(private val plugin: Beidou) : ListenerAdapter() {

    override fun onGuildJoin(event: GuildJoinEvent) {
        // Create folder if it does not exist on guild join.
        this.plugin.createGuildFolder(event.guild)
    }
}