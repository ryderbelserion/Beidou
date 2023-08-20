package com.ryderbelserion.api.plugin.config

import java.io.File

interface FileContext {

    fun addFile(fileEngine: FileEngine?)
    fun saveFile(fileEngine: FileEngine?)
    fun removeFile(fileEngine: FileEngine?)
    fun getFile(fileEngine: FileEngine?): File?

}