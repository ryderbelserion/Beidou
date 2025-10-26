package com.ryderbelserion.bot.configs.types

import com.ryderbelserion.bot.util.ConfigUtil
import com.ryderbelserion.fusion.files.types.configurate.JsonCustomFile

class GuildCache(val file: JsonCustomFile) {

    private val guilds = arrayListOf<String>()

    fun init() {
        val configuration = file.configuration

        val list: List<String> = ConfigUtil.getStringList(configuration, "whitelist")

        this.guilds.clear()

        list.forEach {
            this.guilds.add(it)
        }
    }

    fun getGuilds(): ArrayList<String> {
        return this.guilds
    }
}