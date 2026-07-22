package com.ryderbelserion.discord.bot.storage.impl.file;

import com.ryderbelserion.discord.bot.storage.impl.ConnectionFactory;
import com.zaxxer.hikari.HikariDataSource;
import org.jetbrains.annotations.Nullable;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.CompletableFuture;

public abstract class FlatFactory extends ConnectionFactory {

    protected final String impl;

    public FlatFactory(final String impl) {
        this.impl = impl;
    }

    protected abstract String url();

    protected HikariDataSource source;

    @Override
    public @Nullable Connection getConnection() throws SQLException {
        if (this.source == null) {
            throw new IllegalStateException("Failed to get connection from pool. (Source returned null)");
        }

        if (this.source.isClosed()) {
            throw new IllegalStateException("Failed to get connection from pool. (Source returned closed)");
        }

        final Connection connection = this.source.getConnection();

        if (connection == null) {
            throw new IllegalStateException("Failed to get connection from pool. (getConnection returned null)");
        }

        return connection;
    }

    @Override
    public void init() {
        CompletableFuture.runAsync(() -> {
            try (final Connection connection = getConnection()) {
                if (connection == null) return;

                try (final Statement statement = connection.createStatement()) {
                    statement.addBatch(this.create_guilds_table);

                    statement.executeBatch();
                }
            } catch (final SQLException exception) {
                exception.printStackTrace();
            }
        });
    }

    @Override
    public void stop() {
        if (this.source != null) {
            this.source.close();
        }
    }

    @Override
    public boolean isRunning() {
        return this.source != null && this.source.isRunning();
    }

    @Override
    public String getImpl() {
        return this.impl;
    }
}