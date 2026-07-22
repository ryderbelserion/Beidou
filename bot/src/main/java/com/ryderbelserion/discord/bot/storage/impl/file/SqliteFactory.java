package com.ryderbelserion.discord.bot.storage.impl.file;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.jspecify.annotations.NonNull;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class SqliteFactory extends FlatFactory {
    
    private final Path path;

    public SqliteFactory(@NonNull final Path path) {
        super("SQLite");
        
        this.path = path;
    }

    @Override
    public void init() {
        if (!Files.exists(this.path)) {
            try {
                Files.createFile(this.path);
            } catch (final IOException exception) {
                exception.printStackTrace();
            }
        }

        final HikariConfig config = new HikariConfig();

        config.setJdbcUrl(url());
        config.setMaximumPoolSize(5); // 5 is enough for flat file.
        config.setConnectionInitSql("PRAGMA foreign_keys = ON;");

        this.source = new HikariDataSource(config);

        super.init();
    }

    @Override
    protected String url() {
        return "jdbc:sqlite:" + this.path.toFile().getAbsolutePath();
    }
}