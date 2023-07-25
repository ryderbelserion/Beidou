package com.ryderbelserion.api

import com.ryderbelserion.api.exceptions.ModuleInitializeFailure
import com.ryderbelserion.api.provider.ApiRegistration
import java.io.File

class DedicatedModule : ModuleInterface {

    private var isActive: Boolean = false

    private val dataFolder = File("./bot")

    private val addonFolder = dataFolder.resolve("addons")

    private var moduleApplication: ModuleApplication? = null

    fun get(): ModuleApplication {
        return moduleApplication ?: throw ModuleInitializeFailure("You are fucked.")
    }

    override fun init(): Boolean {
        runCatching {
            if (!this.dataFolder.exists()) this.dataFolder.mkdir()

            if (!this.addonFolder.exists() && this.addonFolder.exists()) this.addonFolder.mkdir()
        }.onFailure {
            this.isActive = false

            throw ModuleInitializeFailure("Could not enable the dedicated module!")
        }.onSuccess {
            this.isActive = true
        }

        return this.isActive
    }

    fun isActive(): Boolean {
        return this.isActive
    }
}

fun configure() {
    // Create the initial module.
    val module = DedicatedModule()
    // Initialize it.
    module.init()
}