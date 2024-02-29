package com.ryderbelserion.common.utils.listeners

import com.ryderbelserion.common.DedicatedModule
import net.dv8tion.jda.api.events.guild.GuildAvailableEvent
import net.dv8tion.jda.api.events.guild.GuildReadyEvent
import net.dv8tion.jda.api.events.session.ReadyEvent
import net.dv8tion.jda.api.events.session.ShutdownEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class ModuleListener(private val dedicatedModule: DedicatedModule, private val module: DedicatedModule.() -> Unit) : ListenerAdapter() {

    override fun onReady(event: ReadyEvent) {
        this.module(this.dedicatedModule)

        this.dedicatedModule.onReady(event.jda)
    }

    override fun onGuildReady(event: GuildReadyEvent) {
        this.dedicatedModule.onGuildReady(event.guild)
    }

    override fun onGuildAvailable(event: GuildAvailableEvent) {
        this.dedicatedModule.onGuildReady(event.guild)
    }

    override fun onShutdown(event: ShutdownEvent) {
        this.dedicatedModule.onStop(event.jda)
    }
}