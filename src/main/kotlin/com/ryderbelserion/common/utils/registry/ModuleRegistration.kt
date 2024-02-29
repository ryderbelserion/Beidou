package com.ryderbelserion.common.utils.registry

import com.ryderbelserion.common.ModulePlugin
import org.jetbrains.annotations.ApiStatus
import java.lang.reflect.Method

object ModuleRegistration {

    private var start: Method? = null
    private var stop: Method? = null

    init {
        try {
            this.start = ModuleProvider::class.java.getDeclaredMethod("start", ModulePlugin::class.java)
            this.start?.setAccessible(true)
            this.stop = ModuleProvider::class.java.getDeclaredMethod("stop")
            this.stop?.setAccessible(true)
        } catch (e: NoSuchMethodException) {
            throw ExceptionInInitializerError(e)
        }
    }

    @ApiStatus.Internal
    fun start(plugin: ModulePlugin?) {
        try {
            this.start?.invoke(null, plugin)
        } catch (exception: Exception) {
            println("[ERROR] Failed to enable plugin")
            println("[ERROR] Reason: " + exception.message)
        }
    }

    @ApiStatus.Internal
    fun stop() {
        try {
            this.stop?.invoke(null)
        } catch (exception: Exception) {
            println("[ERROR] Failed to disable plugin")
            println("[ERROR] Reason: " + exception.message)
        }
    }
}