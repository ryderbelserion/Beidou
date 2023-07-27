package com.ryderbelserion.api

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.Guild

abstract class ModuleApplication {

    abstract fun onReady(jda: JDA)

    abstract fun onStart()

    abstract fun onGuildReady(guild: Guild)

    abstract fun onStop(jda: JDA)

    abstract fun token(): String?

    abstract fun init(): Boolean

}