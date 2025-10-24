package com.ryderbelserion.listeners

import com.ryderbelserion.api.DiscordPlugin
import net.dv8tion.jda.api.events.guild.GuildReadyEvent
import net.dv8tion.jda.api.events.session.ReadyEvent
import net.dv8tion.jda.api.events.session.ShutdownEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class StatusListener(private val plugin: DiscordPlugin) : ListenerAdapter() {

    override fun onGuildReady(event: GuildReadyEvent) {
        this.plugin.onGuildReady(event.guild)
    }

    override fun onShutdown(event: ShutdownEvent) {
        this.plugin.onStop(event.jda)
    }

    override fun onReady(event: ReadyEvent) {
        this.plugin.onReady(event.jda)
    }
}