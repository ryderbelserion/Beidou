package com.ryderbelserion.discord.api.options;

import com.ryderbelserion.discord.api.options.interfaces.IOption;
import com.ryderbelserion.discord.api.options.types.TokenOption;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import org.jetbrains.annotations.NotNull;
import java.util.*;

public class OptionsManager {

    private final Map<String, IOption<?>> cache = new HashMap<>();

    private final OptionParser parser;

    public OptionsManager() {
        this.parser = new OptionParser();
    }

    private OptionSet options;

    public @NotNull final Optional<IOption<?>> getOptionByName(@NotNull final String name) {
        return Optional.ofNullable(this.cache.getOrDefault(name, null));
    }

    public @NotNull final Map<String, IOption<?>> getCache() {
        return Collections.unmodifiableMap(this.cache);
    }

    public @NotNull final Optional<OptionSet> getOption() {
        return Optional.of(this.options);
    }

    public void init(@NotNull final String[] args) {
        List.of(
                new TokenOption(this.parser)
        ).forEach(option -> this.cache.put(option.getName(), option.init()));

        this.options = this.parser.parse(args);
    }
}