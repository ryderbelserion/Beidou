package com.ryderbelserion.discord.bot.storage.impl.hikari;

import com.ryderbelserion.discord.bot.storage.StorageCredentials;
import org.jetbrains.annotations.NotNull;

public class PostgresFactory extends HikariFactory {

    public PostgresFactory(@NotNull final StorageCredentials credentials) {
        super(credentials);
    }

    @Override
    protected @NotNull final String getDriverIdentifier() {
        return "org.postgresql.Driver";
    }

    @Override
    protected @NotNull final String getJDBCIdentifier() {
        return "postgresql";
    }

    @Override
    public @NotNull final String getImplementation() {
        return "PostgresSQL";
    }

    @Override
    protected int getDefaultPort() {
        return 5432;
    }
}