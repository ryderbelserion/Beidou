package com.ryderbelserion.discord.api.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public abstract class CommandEngine extends ListenerAdapter {

    private final String name;
    private final String description;
    private final Permission permission;

    public CommandEngine(@NotNull final String name,
                         @NotNull final String description,
                         @NotNull final Permission permission) {
        this.name = name;
        this.description = description;
        this.permission = permission;
    }

    protected abstract void perform(@NotNull final CommandContext context);

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        final CommandContext context = new CommandContext(event);

        if (!event.getName().equalsIgnoreCase(this.name)) return;

        perform(context);
    }

    public @NotNull final String getName() {
        return this.name;
    }

    public @NotNull final String getDescription() {
        return this.description;
    }

    public @NotNull final Permission getPermission() {
        return this.permission;
    }
}