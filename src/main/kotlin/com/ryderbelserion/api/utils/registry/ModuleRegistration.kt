package com.ryderbelserion.api.utils.registry

import com.ryderbelserion.api.ModulePlugin
import org.jetbrains.annotations.ApiStatus
import java.lang.reflect.Method

object ModuleRegistration {

    private var start: Method? = null
    private var stop: Method? = null

    init {
        try {
            start = ModuleProvider::class.java.getDeclaredMethod("start", ModulePlugin::class.java)
            start?.setAccessible(true)
            stop = ModuleProvider::class.java.getDeclaredMethod("stop")
            stop?.setAccessible(true)
        } catch (e: NoSuchMethodException) {
            throw ExceptionInInitializerError(e)
        }
    }

    @ApiStatus.Internal
    fun start(plugin: ModulePlugin?) {
        try {
            start?.invoke(null, plugin)
        } catch (exception: Exception) {
            println("[ERROR] Failed to enable plugin")
            println("[ERROR] Reason: " + exception.message)
        }
    }

    @ApiStatus.Internal
    fun stop() {
        try {
            stop?.invoke(null)
        } catch (exception: Exception) {
            println("[ERROR] Failed to disable plugin")
            println("[ERROR] Reason: " + exception.message)
        }
    }
}