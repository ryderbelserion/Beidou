package com.ryderbelserion.api

import com.ryderbelserion.api.exceptions.ModuleInitializeException
import com.ryderbelserion.api.listeners.ListenerBuilder
import com.ryderbelserion.api.listeners.ModuleListener
import com.ryderbelserion.api.schedules.Scheduler
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.utils.cache.CacheFlag
import java.io.File

abstract class DedicatedModule(
    private val gateways: List<GatewayIntent> = emptyList(),
    private val cache: List<CacheFlag> = emptyList(),
    private val extra: DedicatedModule.() -> Unit = {}
) : ModuleApplication() {

    private var isActive: Boolean = false

    private val jda: JDA = get()

    private fun get(): JDA {
        return JDABuilder.createDefault(token(), gateways).enableCache(cache).addEventListeners(ModuleListener(this, extra)).build()
    }

    fun DedicatedModule.listeners(configuration: ListenerBuilder.() -> Unit): ListenerBuilder {
        return ListenerBuilder(jda).apply(configuration)
    }

    override fun init(): Boolean {
        runCatching {
            if (!this.dataFolder.exists()) this.dataFolder.mkdir()

            if (!this.addonFolder.exists() && this.addonFolder.exists()) this.addonFolder.mkdir()

            Scheduler.start()

            onStart()
        }.onFailure {
            this.isActive = false

            throw ModuleInitializeException("Could not enable the dedicated module!")
        }.onSuccess {
            this.isActive = true
        }

        return this.isActive
    }

    fun isActive(): Boolean {
        return this.isActive
    }

    val dataFolder = File("./bot")

    val addonFolder = dataFolder.resolve("addons")
}