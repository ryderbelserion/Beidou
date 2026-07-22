package com.ryderbelserion.discord.api.commands.interfaces;

import com.ryderbelserion.discord.api.commands.CommandContext;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.SelfUser;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.jspecify.annotations.NonNull;
import org.jetbrains.annotations.Nullable;

public interface CommandActor {

    /**
     * Sends a simple string with an ephemeral toggle.
     */
    void reply(@NonNull final String message, final boolean ephemeral);

    /**
     * Sends an embed with an ephemeral toggle.
     */
    void reply(@NonNull final MessageEmbed embed, final boolean ephemeral);

    /**
     * Defer a reply, Sends the thinking... message to a user.
     *
     * @return the object
     */
    @NonNull CommandContext defer(final boolean ephemeral);

    /**
     * Gets the option from the event.
     */
    @Nullable OptionMapping getOption(@NonNull final String option);

    /**
     * @author of the message.
     */
    @NonNull User getAuthor();

    /**
     * @return the one who created the bot.
     */
    @NonNull User getCreator();

    /**
     * Checks if the user is the creator of the bot.
     * @param id the id
     * @return true or false
     */
    boolean isCreator(@NonNull final String id);

    /**
     * @return the bot.
     */
    @NonNull SelfUser getBot();

    /**
     * @return guild the command is executed in.
     */
    @Nullable Guild getGuild();

    /**
     * @return jda instance.
     */
    @NonNull JDA getJDA();

}