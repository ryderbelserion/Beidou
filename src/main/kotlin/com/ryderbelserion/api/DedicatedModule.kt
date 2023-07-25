package com.ryderbelserion.api

import com.ryderbelserion.api.exceptions.ModuleInitializeException
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import java.io.File

abstract class DedicatedModule : ModuleApplication {

    private var isActive: Boolean = false

    private val dataFolder = File("./bot")

    private val addonFolder = dataFolder.resolve("addons")

    private lateinit var jda: JDA

    override fun init(): Boolean {
        runCatching {
            if (!this.dataFolder.exists()) this.dataFolder.mkdir()

            if (!this.addonFolder.exists() && this.addonFolder.exists()) this.addonFolder.mkdir()

            this.jda = JDABuilder.createDefault(token()).build()
        }.onFailure {
            this.isActive = false

            throw ModuleInitializeException("Could not enable the dedicated module!")
        }.onSuccess {
            this.isActive = true
        }

        return this.isActive
    }

    fun isActive(): Boolean {
        return this.isActive
    }
}