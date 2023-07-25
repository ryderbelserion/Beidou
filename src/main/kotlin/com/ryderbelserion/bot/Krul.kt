package com.ryderbelserion.bot

import com.ryderbelserion.api.ModuleApplication
import net.dv8tion.jda.api.entities.Guild

class Krul : ModuleApplication {

    override fun onReady() {

    }

    override fun onStart() {

    }

    override fun onGuildReady(guild: Guild) {

    }

    override fun onStop() {

    }

    override fun token(): String? {
        return System.getenv("discord_token")
    }
}