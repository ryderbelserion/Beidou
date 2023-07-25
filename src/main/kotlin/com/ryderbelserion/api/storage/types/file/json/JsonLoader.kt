package com.ryderbelserion.api.storage.types.file.json

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.ryderbelserion.api.storage.FileExtension
import com.ryderbelserion.api.storage.enums.StorageType
import com.ryderbelserion.api.storage.types.file.FileLoader
import java.io.*
import java.lang.reflect.Modifier
import java.nio.charset.StandardCharsets

class JsonLoader(private val fileExtension: FileExtension) : FileLoader {

    private val file: File = fileExtension.file
    private val gson: Gson

    init {
        if (this.fileExtension.builder != null) {
            this.gson = this.fileExtension.builder.create();
        } else {
            val gsonBuilder = GsonBuilder()
                .disableHtmlEscaping()
                .excludeFieldsWithModifiers(Modifier.TRANSIENT)
                .excludeFieldsWithoutExposeAnnotation()

            this.gson = gsonBuilder.create();
        }
    }

    override fun load() {
        runCatching {
            if (file.createNewFile()) {
                save()

                return
            }
        }.onSuccess {
            InputStreamReader(FileInputStream(file), StandardCharsets.UTF_8).use { reader ->
                gson.fromJson(
                    reader,
                    fileExtension.javaClass
                )
            }
        }
    }

    override fun save() {
        runCatching {
            if (!file.exists()) file.createNewFile()

            write()
        }
    }

    override val implName: String get() = StorageType.JSON.type

    private fun write() {
        OutputStreamWriter(FileOutputStream(file), StandardCharsets.UTF_8).use { writer ->
            val values = gson.toJson(fileExtension, fileExtension.javaClass)
            writer.write(values)
        }
    }
}