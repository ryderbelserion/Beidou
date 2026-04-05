package com.ryderbelserion.discord.bot.commands.misc;

import com.ryderbelserion.discord.api.commands.CommandContext;
import com.ryderbelserion.discord.api.commands.CommandEngine;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.jetbrains.annotations.NotNull;

public class FrierenCommand extends CommandEngine {

    public FrierenCommand() {
        super("frieren", "Sends the racist elf.");
    }

    @Override
    protected @NotNull final CommandData getCommandData() {
        return Commands.slash(getName(), getDescription());
    }

    @Override
    protected void perform(
            @NotNull final SlashCommandInteractionEvent event,
            @NotNull final CommandContext context
    ) {
        context.reply("https://cdn.ryderbelserion.com/frieren.mp4", false);
    }
}