package com.ryderbelserion.bot.configs

import com.ryderbelserion.api.builders.files.FileManager
import com.ryderbelserion.api.builders.files.types.JsonCustomFile
import com.ryderbelserion.bot.configs.types.GuildCache
import java.nio.file.Path

class ConfigManager(val fileManager: FileManager?, val path: Path) {

    private var guildCache: GuildCache? = null

    fun init() {
        this.fileManager?.getFile(this.path.resolve("guilds.json"))?.ifPresent { file ->
            this.guildCache = GuildCache(file as JsonCustomFile)
        }

        this.guildCache?.init()
    }

    fun reload() {
        this.guildCache?.init()
    }

    fun getGuildCache(): GuildCache? {
        return this.guildCache
    }
}