package com.ryderbelserion.bot.util

import org.spongepowered.configurate.BasicConfigurationNode

object ConfigUtil {

    fun getStringList(configuration: BasicConfigurationNode, defaultValues: List<String>, vararg path: Any): List<String?> {
        return configuration.node(path).getList(String::class.java) ?: defaultValues
    }

    fun getStringList(configuration: BasicConfigurationNode, vararg path: Any): List<String?> {
        return getStringList(configuration, path)
    }
}