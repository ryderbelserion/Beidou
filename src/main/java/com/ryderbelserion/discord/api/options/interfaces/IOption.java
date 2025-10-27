package com.ryderbelserion.discord.api.options.interfaces;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import org.jetbrains.annotations.NotNull;
import java.util.Optional;

public abstract class IOption<T> {

    protected final OptionParser parser;

    public IOption(@NotNull final OptionParser parser) {
        this.parser = parser;
    }

    public abstract Optional<T> getValue(@NotNull final OptionSet option);

    public abstract String getDescription();

    public abstract IOption<T> init();

    public abstract String getName();

}