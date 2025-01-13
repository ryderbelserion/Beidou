package com.ryderbelserion.api.util.registry

import com.ryderbelserion.api.VitalDiscord
import org.jetbrains.annotations.ApiStatus

public object ModuleProvider {

    private var plugin: VitalDiscord? = null

    public fun get(): VitalDiscord {
        return this.plugin ?: throw RuntimeException("Failed to utilize plugin. Did it get enabled?")
    }

    @ApiStatus.Internal
    public fun start(plugin: VitalDiscord) {
        ModuleProvider.plugin = plugin;
    }

    @JvmStatic
    @ApiStatus.Internal
    public fun stop() {
        this.plugin = null
    }
}