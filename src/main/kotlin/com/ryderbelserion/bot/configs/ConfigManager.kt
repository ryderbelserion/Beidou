package com.ryderbelserion.bot.configs

import com.ryderbelserion.bot.configs.types.GuildCache
import com.ryderbelserion.fusion.files.FileManager
import java.nio.file.Path

class ConfigManager(val fileManager: FileManager?, val path: Path) {

    private var guildCache: GuildCache? = null

    fun init() {
        this.fileManager?.getJsonFile(this.path.resolve("guilds.json"))?.ifPresent { file ->
            this.guildCache = GuildCache(file)
        }

        this.guildCache?.init()
    }

    fun getGuildCache(): GuildCache? {
        return this.guildCache
    }

    fun reload() {
        this.guildCache?.init()
    }
}