package com.ryderbelserion.discord.bot.api.environment.enums;

import org.jetbrains.annotations.NotNull;

public enum Environment {

    RELEASE("Release"),
    DEVELOPMENT("Development"),
    NONE("None");

    private final String identifier;

    Environment(@NotNull final String identifier) {
        this.identifier = identifier;
    }

    public static @NotNull Environment get(@NotNull final String identifier) {
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

    public @NotNull final String getIdentifier() {
        return this.identifier;
    }
}