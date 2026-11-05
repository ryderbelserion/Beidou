package com.ryderbelserion.discord.bot.storage.impl;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class ConnectionFactory {

    protected final String create_guilds_table = "create table if not exists guilds(guild_id varchar(36) primary key)"; // a database of whitelisted guilds, 1190524968048529558 is my test environment.

    // %s is replaced with the guild id, as this is for the guild's user data.
    protected final String create_users_table = "create table if not exists %s(guild_id varchar(36) primary key not null, foreign key(guild_id) references guilds(guild_id))"; // no delete cascade just in case I fuck something up

    public abstract Connection getConnection() throws SQLException;

    public abstract boolean isRunning();

    public abstract String getImpl();

    public abstract void init();

    public abstract void stop();

    public abstract void addGuild(final String id);

    public abstract void removeGuild(final String id);

    public abstract boolean hasGuild(final String id);

}