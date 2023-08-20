package com.ryderbelserion.api.plugin.config.types.file

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.ryderbelserion.api.plugin.config.FileEngine
import java.io.*
import java.lang.reflect.Modifier
import java.nio.charset.StandardCharsets

class JsonFile(private val context: FileEngine) {

    private val gson: Gson

    init {
        if (this.context.builder != null) {
            this.gson = this.context.builder.create()
        } else {
            val builder = GsonBuilder()
                .disableHtmlEscaping()
                .excludeFieldsWithModifiers(Modifier.TRANSIENT)
                .excludeFieldsWithoutExposeAnnotation()

            this.gson = builder.create()
        }
    }

    fun load() {
        runCatching {
            if (this.context.file.createNewFile()) {
                save()

                return
            }
        }.onSuccess {
            InputStreamReader(FileInputStream(this.context.file), StandardCharsets.UTF_8).use { reader ->
                gson.fromJson(
                    reader,
                    this.context.javaClass
                )
            }

            this.context.load()
        }
    }

    fun save() {
        runCatching {
            if (!this.context.file.exists()) this.context.file.createNewFile()

            write()
        }.onSuccess {
            this.context.save()
        }
    }

    private fun write() {
        OutputStreamWriter(FileOutputStream(this.context.file), StandardCharsets.UTF_8).use { writer ->
            val values = gson.toJson(this.context, this.context.javaClass)
            writer.write(values)
        }
    }
}