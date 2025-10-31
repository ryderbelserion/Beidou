package com.ryderbelserion.discord.api.options.types;

import com.ryderbelserion.discord.api.options.interfaces.IOption;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import org.jetbrains.annotations.NotNull;
import java.util.Optional;

public class TokenOption extends IOption<String> {

    public TokenOption(@NotNull final OptionParser parser) {
        super(parser);
    }

    @Override
    public @NotNull final Optional<String> getValue(@NotNull final OptionSet option) {
        return Optional.ofNullable(option.valueOf(getName()).toString());
    }

    @Override
    public @NotNull final String getDescription() {
        return "Provides a discord bot token to the application.";
    }

    @Override
    public @NotNull final String getName() {
        return "token";
    }

    @Override
    public final IOption<String> init() {
        this.parser.accepts(getName()).withRequiredArg().ofType(String.class).defaultsTo("token_not_found").describedAs(getDescription());

        return this;
    }
}