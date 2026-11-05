package com.ryderbelserion.discord.bot.storage.holder;

import com.ryderbelserion.discord.bot.storage.impl.ConnectionFactory;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class StorageHolder {

    private final ConnectionFactory factory;

    public StorageHolder(final ConnectionFactory factory) {
        this.factory = factory;
    }

    public StorageHolder initialize() {
        this.factory.init();

        return this;
    }

    public StorageHolder addGuild(final String id) {
        this.factory.addGuild(id);

        return this;
    }

    public StorageHolder removeGuild(final String id) {
        this.factory.removeGuild(id);

        return this;
    }

    public boolean hasGuild(final String id) {
        return this.factory.hasGuild(id);
    }

    public void stop() {
        this.factory.stop();
    }
}