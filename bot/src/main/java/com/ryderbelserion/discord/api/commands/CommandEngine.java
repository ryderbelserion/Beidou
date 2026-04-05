package com.ryderbelserion.discord.api.commands;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.AutoCompleteQuery;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.jetbrains.annotations.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class CommandEngine {

    private final Map<String, List<String>> values = new HashMap<>();

    protected final String description;
    protected final String name;

    public CommandEngine(
            @NotNull final String name,
            @NotNull final String description
    ) {
        this.name = name;
        this.description = description;
    }

    protected abstract CommandData getCommandData();

    protected abstract void perform(@NotNull final SlashCommandInteractionEvent event, @NotNull final CommandContext context);

    public void interact(@NotNull final SlashCommandInteractionEvent event) {
        final CommandContext context = new CommandContext(event);

        if (!event.getName().equalsIgnoreCase(this.name)) return;

        final User user = context.getAuthor();

        if (user.isBot()) return; // don't let bots interact with the command

        perform(event, context);
    }

    public void complete(@NotNull final CommandAutoCompleteInteractionEvent event) {
        final String name = event.getName();

        if (!name.equalsIgnoreCase(this.name)) return;

        final AutoCompleteQuery option = event.getFocusedOption();

        final String optionName = option.getName();

        if (!this.values.containsKey(optionName)) return;

        final List<String> values = this.values.get(optionName);

        final List<Command.Choice> choices = values.stream().filter(word -> word.startsWith(option.getValue())).map(word -> new Command.Choice(word, word)).toList();

        event.replyChoices(choices).queue();
    }

    public void addChoice(@NotNull final String option, @NotNull final List<String> values) {
        this.values.put(option, values);
    }

    public void removeChoice(@NotNull final String option) {
        this.values.remove(option);
    }

    public @NotNull final String getDescription() {
        return this.description;
    }

    public @NotNull final String getName() {
        return this.name;
    }
}