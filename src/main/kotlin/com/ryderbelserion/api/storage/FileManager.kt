package com.ryderbelserion.api.storage

import java.io.File

interface FileManager {

    fun addFile(fileExtension: FileExtension?)
    fun saveFile(fileExtension: FileExtension?)
    fun removeFile(fileExtension: FileExtension?)
    fun getFile(fileExtension: FileExtension?): File?

}