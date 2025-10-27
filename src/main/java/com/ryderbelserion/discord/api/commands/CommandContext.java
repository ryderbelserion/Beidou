package com.ryderbelserion.discord.api.commands;

import com.ryderbelserion.discord.api.commands.interfaces.CommandActor;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.SelfUser;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.jetbrains.annotations.NotNull;

public class CommandContext implements CommandActor {

    private final SlashCommandInteractionEvent event;

    public CommandContext(@NotNull final SlashCommandInteractionEvent event) {
        this.event = event;
    }

    @Override
    public void reply(@NotNull final String message, final boolean ephemeral) {
        this.event.reply(message).setEphemeral(ephemeral).queue();
    }

    @Override
    public void reply(@NotNull final MessageEmbed embed, final boolean ephemeral) {
        this.event.replyEmbeds(embed).setEphemeral(ephemeral).queue();
    }

    @Override
    public CommandContext defer(final boolean ephemeral) {
        this.event.deferReply(ephemeral).queue();

        return this;
    }

    @Override
    public OptionMapping getOption(@NotNull final String option) {
        return this.event.getOption(option);
    }

    @Override
    public User getAuthor() {
        return this.event.getUser();
    }

    @Override
    public User getCreator() {
        return getJDA().getUserById("209853986646261762");
    }

    @Override
    public SelfUser getBot() {
        return getJDA().getSelfUser();
    }

    @Override
    public Guild getGuild() {
        return this.event.getGuild();
    }

    @Override
    public JDA getJDA() {
        return this.event.getJDA();
    }
}