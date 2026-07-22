package com.ryderbelserion.discord.api.options.types;

import com.ryderbelserion.discord.api.options.interfaces.IOption;
import com.ryderbelserion.discord.bot.api.environment.enums.Environment;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import org.jspecify.annotations.NonNull;
import java.util.Optional;

public class EnvOption extends IOption<Environment> {

    public EnvOption(@NonNull final OptionParser parser) {
        super(parser);
    }

    @Override
    public @NonNull final Optional<Environment> getValue(@NonNull final OptionSet option) {
        final String value = option.valueOf(getName()).toString();

        return Optional.of(Environment.get(value));
    }

    @Override
    public @NonNull final String getDescription() {
        return "Tells the bot what environment to run in. Defaults to RELEASE";
    }

    @Override
    public @NonNull final String getName() {
        return "environment";
    }

    @Override
    public final IOption<Environment> init() {
        this.parser.accepts(getName()).withOptionalArg().ofType(Environment.class).defaultsTo(Environment.RELEASE).describedAs(getDescription());

        return this;
    }
}