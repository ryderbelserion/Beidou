package com.ryderbelserion.bot.listeners

import com.ryderbelserion.bot.Beidou
import net.dv8tion.jda.api.events.guild.GuildJoinEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class GuildListener(private val plugin: Beidou) : ListenerAdapter() {

    override fun onGuildJoin(event: GuildJoinEvent) {

    }
}