package com.ryderbelserion.api.plugin.registry

import com.ryderbelserion.api.plugin.ModulePlugin
import org.jetbrains.annotations.ApiStatus

object ModuleProvider {

    private var plugin: ModulePlugin? = null

    fun get(): ModulePlugin {
        return plugin ?: throw RuntimeException("Failed to utilize plugin. Did it get enabled?")
    }

    @ApiStatus.Internal
    fun start(plugin: ModulePlugin) {
        ModuleProvider.plugin = plugin;
    }

    @JvmStatic
    @ApiStatus.Internal
    fun stop() {
        plugin = null
    }
}