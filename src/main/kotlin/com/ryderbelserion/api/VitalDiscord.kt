package com.ryderbelserion.api

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.Guild
import java.io.File

public abstract class VitalDiscord(private val pluginName: String?) {

    public abstract fun init(): Boolean

    public abstract fun onGuildReady(guild: Guild)

    public abstract fun onReady(jda: JDA)

    public abstract fun onStop(jda: JDA)

    public abstract fun onStart()

    public abstract fun token(): String?

    public abstract fun isActive(): Boolean

    public fun getDirectory(): File {
        return File(this.pluginName.toString())
    }

    public fun getGuildDirectory(id: String?): File {
        return getGuildDirectory().resolve(id.toString())
    }

    public fun getGuildDirectory(): File {
        return getDirectory().resolve("guilds")
    }

    public fun getAddonDirectory(): File {
        return getDirectory().resolve("addons")
    }

    public fun getCacheDirectory(): File {
        return getDirectory().resolve("cache")
    }
}