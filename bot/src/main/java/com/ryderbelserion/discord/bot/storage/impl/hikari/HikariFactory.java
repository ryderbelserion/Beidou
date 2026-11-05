package com.ryderbelserion.discord.bot.storage.impl.hikari;

import com.ryderbelserion.discord.bot.storage.StorageCredentials;
import com.ryderbelserion.discord.bot.storage.impl.file.FlatFactory;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.jspecify.annotations.NonNull;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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

    public void configure(
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

        super.init();
    }

    @Override
    public void addGuild(final String id) {
        CompletableFuture.runAsync(() -> {
            try (final Connection connection = this.source.getConnection(); final PreparedStatement statement =
                    connection.prepareStatement("insert into guilds(guild_id) values (?)")) {
                statement.setString(1, id);

                statement.executeUpdate();
            } catch (final SQLException exception) {
                exception.printStackTrace();
            }
        });
    }

    @Override
    public void removeGuild(final String id) {
        CompletableFuture.runAsync(() -> {
            try (final Connection connection = this.source.getConnection(); final PreparedStatement statement =
                    connection.prepareStatement("delete from guilds where guild_id=?")) {
                statement.setString(1, id);

                statement.executeUpdate();
            } catch (final SQLException exception) {
                exception.printStackTrace();
            }
        });
    }

    @Override
    public boolean hasGuild(final String id) {
        return CompletableFuture.supplyAsync(() -> {
            try (final Connection connection = this.source.getConnection(); final PreparedStatement statement =
                    connection.prepareStatement("select 1 from guilds where guild_id=? limit 1")) {
                statement.setString(1, id);

                return statement.executeQuery().next();
            } catch (final SQLException exception) {
                exception.printStackTrace();
            }

            return false;
        }).join();
    }
}