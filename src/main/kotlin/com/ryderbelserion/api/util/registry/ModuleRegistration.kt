package com.ryderbelserion.api.util.registry

import com.ryderbelserion.api.VitalDiscord
import java.lang.reflect.Method

public object ModuleRegistration {

    private var start: Method? = null
    private var stop: Method? = null

    init {
        try {
            start = ModuleProvider::class.java.getDeclaredMethod("start", VitalDiscord::class.java)
            start?.setAccessible(true)
            stop = ModuleProvider::class.java.getDeclaredMethod("stop")
            stop?.setAccessible(true)
        } catch (e: NoSuchMethodException) {
            throw ExceptionInInitializerError(e)
        }
    }

    public fun start(plugin: VitalDiscord?) {
        try {
            start?.invoke(null, plugin)
        } catch (exception: Exception) {
            println("[ERROR] Failed to enable plugin")
            println("[ERROR] Reason: " + exception.message)
        }
    }

    public fun stop() {
        try {
            stop?.invoke(null)
        } catch (exception: Exception) {
            println("[ERROR] Failed to disable plugin")
            println("[ERROR] Reason: " + exception.message)
        }
    }
}