package com.ryderbelserion.discord.api.commands.interfaces;

import com.ryderbelserion.discord.api.commands.CommandContext;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.SelfUser;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.jetbrains.annotations.NotNull;

public interface CommandActor {

    /**
     * Sends a simple string with an ephemeral toggle.
     */
    void reply(@NotNull final String message, final boolean ephemeral);

    /**
     * Sends an embed with an ephemeral toggle.
     */
    void reply(@NotNull final MessageEmbed embed, final boolean ephemeral);

    /**
     * Defer a reply, Sends the thinking... message to a user.
     *
     * @return the object
     */
    CommandContext defer(final boolean ephemeral);

    /**
     * Gets the option from the event.
     */
    OptionMapping getOption(@NotNull final String option);

    /**
     * @author of the message.
     */
    User getAuthor();

    /**
     * @return the one who created the bot.
     */
    User getCreator();

    /**
     * @return the bot.
     */
    SelfUser getBot();

    /**
     * @return guild the command is executed in.
     */
    Guild getGuild();

    /**
     * @return jda instance.
     */
    JDA getJDA();

}