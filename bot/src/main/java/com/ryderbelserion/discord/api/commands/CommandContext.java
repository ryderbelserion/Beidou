package com.ryderbelserion.discord.api.commands;

import com.ryderbelserion.discord.api.commands.interfaces.CommandActor;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.SelfUser;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.jspecify.annotations.NonNull;
import org.jetbrains.annotations.Nullable;
import java.util.Objects;

public class CommandContext implements CommandActor {

    private final SlashCommandInteractionEvent event;

    public CommandContext(@NonNull final SlashCommandInteractionEvent event) {
        this.event = event;
    }

    @Override
    public final void reply(@NonNull final String message, final boolean ephemeral) {
        this.event.reply(message).setEphemeral(ephemeral).queue();
    }

    @Override
    public final void reply(@NonNull final MessageEmbed embed, final boolean ephemeral) {
        this.event.replyEmbeds(embed).setEphemeral(ephemeral).queue();
    }

    @Override
    public @NonNull final CommandContext defer(final boolean ephemeral) {
        this.event.deferReply(ephemeral).queue();

        return this;
    }

    @Override
    public @Nullable final OptionMapping getOption(@NonNull final String option) {
        return this.event.getOption(option);
    }

    @Override
    public @NonNull final User getAuthor() {
        return this.event.getUser();
    }

    @Override
    public final boolean isCreator(@NonNull final String id) {
        return id.equalsIgnoreCase(getCreator().getId());
    }

    @Override
    public @NonNull final User getCreator() {
        return Objects.requireNonNull(getJDA().getUserById("209853986646261762"));
    }

    @Override
    public @NonNull final SelfUser getBot() {
        return getJDA().getSelfUser();
    }

    @Override
    public @Nullable final Guild getGuild() {
        return this.event.getGuild();
    }

    @Override
    public @NonNull final JDA getJDA() {
        return this.event.getJDA();
    }
}