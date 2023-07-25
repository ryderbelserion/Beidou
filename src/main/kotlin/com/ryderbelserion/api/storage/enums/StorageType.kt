package com.ryderbelserion.api.storage.enums

enum class StorageType(val type: String, vararg identifiers: String) {

    // Config style databases
    JSON("JSON", "json"),

    // Local databases
    SQLITE("SQLite", "sqlite");

    private val identifiers: List<String>

    init {
        this.identifiers = listOf(*identifiers)
    }
}