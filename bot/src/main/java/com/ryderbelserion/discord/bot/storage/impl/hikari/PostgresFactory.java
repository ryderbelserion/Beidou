package com.ryderbelserion.discord.bot.storage.impl.hikari;

import com.ryderbelserion.discord.bot.storage.StorageCredentials;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class PostgresFactory extends HikariFactory {

    public PostgresFactory(final StorageCredentials credentials) {
        super(credentials, "PostgresSQL");
    }

    @Override
    public String getDriverIdentifier() {
        return "org.postgresql.Driver";
    }

    @Override
    public String getJDBCIdentifier() {
        return "postgresql";
    }

    @Override
    public int getDefaultPort() {
        return 5432;
    }

    @Override
    public String url() {
        return "";
    }
}