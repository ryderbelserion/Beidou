package com.ryderbelserion.api

import net.dv8tion.jda.api.entities.Guild

interface ModuleApplication {

    fun onReady()

    fun onStart()

    fun onGuildReady(guild: Guild)

    fun onStop()

    fun token(): String?

}