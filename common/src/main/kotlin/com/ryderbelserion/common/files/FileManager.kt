package com.ryderbelserion.common.files

import com.ryderbelserion.common.files.types.FileType
import com.ryderbelserion.common.files.types.file.JsonFile
import java.io.File

class FileManager : FileContext {

    private var jsonFile: JsonFile? = null

    override fun addFile(fileEngine: FileEngine?) {
        when (fileEngine?.type) {
            FileType.JSON -> {
                jsonFile = JsonFile(fileEngine)
                jsonFile?.load()
            }

            else -> throw IllegalStateException("Unexpected value: " + fileEngine?.type)
        }
    }

    override fun saveFile(fileEngine: FileEngine?) {
        when (fileEngine?.type) {
            FileType.JSON -> {
                jsonFile = JsonFile(fileEngine)
                jsonFile?.save()
            }

            else -> throw IllegalStateException("Unexpected value: " + fileEngine?.type)
        }
    }

    override fun removeFile(fileEngine: FileEngine?) {
        val file = fileEngine?.path?.toFile()
        if (file?.exists() == true) file.delete()
    }

    override fun getFile(fileEngine: FileEngine?): File? {
        return fileEngine?.file
    }
}