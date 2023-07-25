package com.ryderbelserion.api.storage.types.file

interface FileLoader {

    fun load()

    fun save()

    val implName: String?

}