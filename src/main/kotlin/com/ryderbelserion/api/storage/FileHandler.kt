package com.ryderbelserion.api.storage

import com.ryderbelserion.api.storage.enums.StorageType
import com.ryderbelserion.api.storage.types.file.json.JsonLoader
import com.ryderbelserion.alexandrite.api.utils.FileUtils
import java.io.File
import java.nio.file.Path

class FileHandler : FileManager {

    private var jsonLoader: JsonLoader? = null

    override fun addFile(fileExtension: FileExtension?) {
        when (fileExtension?.type) {
            StorageType.JSON -> {
                jsonLoader = JsonLoader(fileExtension)
                jsonLoader?.load()
            }

            else -> throw IllegalStateException("Unexpected value: " + fileExtension?.type)
        }
    }

    override fun saveFile(fileExtension: FileExtension?) {
        when (fileExtension?.type) {
            StorageType.JSON -> {
                jsonLoader = JsonLoader(fileExtension)
                jsonLoader?.save()
            }

            else -> throw IllegalStateException("Unexpected value: " + fileExtension?.type)
        }
    }

    override fun removeFile(fileExtension: FileExtension?) {
        val file = fileExtension?.path?.toFile()
        if (file?.exists() == true) file.delete()
    }

    override fun getFile(fileExtension: FileExtension?): File? {
        return fileExtension?.file
    }

    private fun extract(value: String, directory: Path) {
        File(directory.toString() + value).mkdir()

        FileUtils.extract(value, directory, false)
    }
}