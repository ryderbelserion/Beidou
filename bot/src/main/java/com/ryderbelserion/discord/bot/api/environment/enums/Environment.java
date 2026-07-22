package com.ryderbelserion.discord.bot.api.environment.enums;

import org.jspecify.annotations.NonNull;

public enum Environment {

    RELEASE("Release"),
    DEVELOPMENT("Development"),
    NONE("None");

    private final String identifier;

    Environment(@NonNull final String identifier) {
        this.identifier = identifier;
    }

    public static @NonNull Environment get(@NonNull final String identifier) {
        Environment environment = Environment.NONE;

        for (final Environment value : Environment.values()) {
            if (!value.identifier.equalsIgnoreCase(identifier)) {
                continue;
            }

            environment = value;

            break;
        }

        return environment;
    }

    public @NonNull final String getIdentifier() {
        return this.identifier;
    }
}