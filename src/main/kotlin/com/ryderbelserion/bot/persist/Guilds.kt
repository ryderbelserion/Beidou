package com.ryderbelserion.bot.persist

import com.ryderbelserion.api.plugin.config.FileEngine
import com.ryderbelserion.api.plugin.config.types.ConfigType
import java.io.File

class Guilds(file: File) : FileEngine("guild.json", file.toPath(), ConfigType.JSON) {

    private val channel_log = hashSetOf<String>()

    override fun load() {

    }

    override fun save() {

    }
}