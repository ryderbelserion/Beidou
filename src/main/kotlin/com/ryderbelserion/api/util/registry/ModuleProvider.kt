package com.ryderbelserion.api.util.registry

import com.ryderbelserion.api.VitalDiscord

public object ModuleProvider {

    private var plugin: VitalDiscord? = null

    public fun get(): VitalDiscord {
        return this.plugin ?: throw RuntimeException("Failed to utilize plugin. Did it get enabled?")
    }

    public fun start(plugin: VitalDiscord) {
        ModuleProvider.plugin = plugin;
    }

    @JvmStatic
    public fun stop() {
        this.plugin = null
    }
}