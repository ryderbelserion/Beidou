package com.ryderbelserion.discord.bot.storage.impl.hikari;

import com.ryderbelserion.discord.bot.storage.StorageCredentials;
import com.ryderbelserion.discord.bot.storage.impl.file.FlatFactory;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.jspecify.annotations.NonNull;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.CompletableFuture;

public abstract class HikariFactory extends FlatFactory {

    private final StorageCredentials credentials;

    public HikariFactory(final StorageCredentials credentials, final String impl) {
        super(impl);
        
        this.credentials = credentials;
    }

    protected abstract String getDriverIdentifier();

    protected abstract String getJDBCIdentifier();

    protected abstract int getDefaultPort();

    protected void configure(
            @NonNull final HikariConfig config,
            @NonNull final String database,
            @NonNull final String username,
            @NonNull final String password,
            @NonNull final String address,
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
}