package com.ryderbelserion.api.listeners

import com.ryderbelserion.api.DedicatedModule
import net.dv8tion.jda.api.events.guild.GuildReadyEvent
import net.dv8tion.jda.api.events.session.ReadyEvent
import net.dv8tion.jda.api.events.session.ShutdownEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

public class ModuleListener(private val plugin: DedicatedModule, private val module: DedicatedModule.() -> Unit) : ListenerAdapter() {

    override fun onReady(event: ReadyEvent) {
        this.module(this.plugin)

        this.plugin.onReady(event.jda)
    }

    override fun onShutdown(event: ShutdownEvent) {
        this.plugin.onStop(event.jda)
    }

    override fun onGuildReady(event: GuildReadyEvent) {
        this.plugin.onGuildReady(event.guild)
    }
}