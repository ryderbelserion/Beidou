package com.ryderbelserion.api.utils

import com.ryderbelserion.api.plugin.ModulePlugin
import com.ryderbelserion.api.plugin.registry.ModuleProvider
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.nio.file.Path
import java.util.function.Consumer

object FileUtils {

    private val plugin: ModulePlugin = ModuleProvider.get()

    fun copyFiles(directory: Path, folder: String, names: List<String>) {
        names.forEach(Consumer { name: String ->
            copyFile(
                directory,
                folder,
                name
            )
        })
    }

    fun copyFile(directory: Path, folder: String, name: String) {
        val file = directory.resolve(name).toFile()
        if (file.exists()) return

        val dir = directory.toFile()
        if (!dir.exists()) dir.mkdirs()

        val loader = javaClass.getClassLoader()
        val url = "$folder/$name"

        val resource = loader?.getResource(url)

        runCatching {
            resource?.openStream()?.let { grab(it, file) }
        }.onFailure {
            // TODO() Add logger
        }
    }

    @Throws(Exception::class)
    private fun grab(input: InputStream, output: File) {
        input.use { inputStream ->
            FileOutputStream(output).use { outputStream ->
                val buf = ByteArray(1024)
                var amount: Int

                while (inputStream.read(buf).also {
                    amount = it
                    } != -1) { outputStream.write(buf, 0, amount) }
            }
        }
    }
}