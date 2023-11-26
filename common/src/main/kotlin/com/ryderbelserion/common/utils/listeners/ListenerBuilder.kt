package com.ryderbelserion.common.utils.listeners

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.hooks.ListenerAdapter

class ListenerBuilder(private val jda: JDA) {

    /**
     * Registers a listener.
     */
    fun register(listener: ListenerAdapter) {
        jda.addEventListener(listener)
    }

    /**
     * Registers multiple listeners.
     */
    fun register(vararg listeners: ListenerAdapter) {
        listeners.forEach(::register)
    }
}