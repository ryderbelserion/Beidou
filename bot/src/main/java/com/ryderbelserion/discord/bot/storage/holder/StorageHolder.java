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

    public void stop() {
        this.factory.stop();
    }
}