package com.ryderbelserion.discord.bot.storage.impl.objects;

import com.ryderbelserion.discord.bot.storage.impl.ConnectionFactory;
import org.jetbrains.annotations.NotNull;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class StorageHolder {

    private final ConnectionFactory factory;

    public StorageHolder(@NotNull final ConnectionFactory factory) {
        this.factory = factory;
    }

    public StorageHolder initialize() {
        this.factory.init();

        return this;
    }

    public void stop() {
        this.factory.stop();
    }

    private List<String> getTables(@NotNull final Connection connection) throws SQLException {
        final List<String> tables = new ArrayList<>();

        try (final ResultSet result = connection.getMetaData().getTables(connection.getCatalog(), null, "%", null)) {
            while (result.next()) {
                tables.add(result.getString(3).toLowerCase(Locale.ROOT));
            }
        }

        return tables;
    }
}