package com.ryderbelserion.discord.bot.storage.impl;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class ConnectionFactory {

    protected final String create_guilds_table = "create table if not exists guilds(id varchar(18) primary key)";

    // %s is replaced with the guild id, as this is for the guild's user data.
    protected final String create_users_table = "create table if not exists %s(id varchar(18) primary key not null, foreign key(id) references guilds(id) on delete cascade)";

    public abstract Connection getConnection() throws SQLException;

    public abstract String getImplementation();

    public abstract boolean isRunning();

    public abstract void init();

    public abstract void stop();

}