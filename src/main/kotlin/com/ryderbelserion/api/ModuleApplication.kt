package com.ryderbelserion.api

import net.dv8tion.jda.api.entities.Guild

abstract class ModuleApplication {

    abstract fun onReady()

    abstract fun onStart()

    abstract fun onGuildReady(guild: Guild)

    abstract fun onStop()

    abstract fun token(): String?

    abstract fun init(): Boolean

}