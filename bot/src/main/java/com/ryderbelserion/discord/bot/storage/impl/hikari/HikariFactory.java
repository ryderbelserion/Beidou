package com.ryderbelserion.discord.bot.storage.impl.hikari;

import com.ryderbelserion.discord.bot.storage.StorageCredentials;
import com.ryderbelserion.discord.bot.storage.impl.ConnectionFactory;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.jetbrains.annotations.NotNull;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.CompletableFuture;

public abstract class HikariFactory extends ConnectionFactory {

    private final StorageCredentials credentials;

    private HikariDataSource source;

    public HikariFactory(@NotNull final StorageCredentials credentials) {
        this.credentials = credentials;
    }

    protected abstract String getDriverIdentifier();

    protected abstract String getJDBCIdentifier();

    protected abstract int getDefaultPort();

    protected void configure(
            @NotNull final HikariConfig config,
            @NotNull final String database,
            @NotNull final String username,
            @NotNull final String password,
            @NotNull final String address,
            final int port
    ) {
        config.setDriverClassName(getDriverIdentifier());

        config.setJdbcUrl(String.format("jdbc:%s://%s:%s/%s", getJDBCIdentifier(), address, port == -1 ? getDefaultPort() : port, database));

        config.setUsername(username);
        config.setPassword(password);
    }

    @Override
    public void init() {
        final HikariConfig config = new HikariConfig();

        config.setPoolName("beidou-hikari");

        configure(
                config,
                this.credentials.database(),
                this.credentials.username(),
                this.credentials.password(),
                this.credentials.address(),
                this.credentials.port()
        );

        config.setMaximumPoolSize(this.credentials.maxPoolSize());
        config.setMinimumIdle(this.credentials.minIdle());
        config.setMaxLifetime(this.credentials.maxLifetime());
        config.setKeepaliveTime(this.credentials.heartbeat());
        config.setConnectionTimeout(this.credentials.connectionTimeout());

        config.setInitializationFailTimeout(-1);

        this.source = new HikariDataSource(config);

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
    public Connection getConnection() throws SQLException {
        if (this.source == null) {
            throw new IllegalStateException("Failed to get connection from pool. (Source returned null)");
        }

        final Connection connection = this.source.getConnection();

        if (connection == null) {
            throw new IllegalStateException("Failed to get connection from pool. (getConnection returned null)");
        }

        return connection;
    }

    @Override
    public boolean isRunning() {
        return this.source != null && this.source.isRunning();
    }
}