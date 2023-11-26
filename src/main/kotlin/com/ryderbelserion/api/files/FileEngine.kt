package com.ryderbelserion.api.files

import com.google.gson.GsonBuilder
import com.ryderbelserion.api.files.types.ConfigType
import java.io.File
import java.nio.file.Path

abstract class FileEngine(private val name: String, @JvmField val path: Path, @JvmField val type: ConfigType) {

    val file: File get() = File(path.toFile(), name)

    val builder: GsonBuilder? = null

}