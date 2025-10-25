package com.ryderbelserion.bot.configs.types

import com.ryderbelserion.api.builders.files.types.JsonCustomFile
import com.ryderbelserion.bot.util.ConfigUtil

class GuildCache(val file: JsonCustomFile) {

    private val guilds = arrayListOf<Long>()

    fun init() {
        val configuration = file.configuration

        val list = ConfigUtil.getStringList(configuration, "whitelist")

        this.guilds.clear()

        list.forEach {
            it?.toLong()?.let { number -> this.guilds.add(number) }
        }
    }

    fun getGuilds(): ArrayList<Long> {
        return this.guilds
    }
}