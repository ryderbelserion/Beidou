package com.ryderbelserion.discord.bot.api.objects.commands;

import com.ryderbelserion.discord.api.utils.ConfigUtils;
import com.ryderbelserion.discord.api.utils.StringUtils;
import com.ryderbelserion.discord.bot.api.managers.EmbedManager;
import com.ryderbelserion.discord.bot.api.objects.BeidouEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.CommentedConfigurationNode;

public class BeidouCommand {

    private final String command;
    private final String description;
    private final boolean isSlash;
    private final BeidouEmbed defaultEmbed;
    private final String defaultMessage;

    public BeidouCommand(@NotNull final String id, @NotNull final CommentedConfigurationNode configuration, @NotNull final EmbedManager manager) {
        this.command = configuration.node("name").getString("");
        this.description = configuration.node("description").getString("");
        this.isSlash = configuration.node("is-slash").getBoolean(true);
        this.defaultEmbed = manager.getEmbed(id, configuration.node("default").getString("")).orElse(null);
        this.defaultMessage = StringUtils.toString(ConfigUtils.getStringList(configuration.node("message")));
    }

    public void interact(@NotNull final SlashCommandInteractionEvent event) {
        if (this.command.isBlank()) return;

        if (!this.isSlash) return;

        final String name = event.getName();

        if (!name.equalsIgnoreCase(this.command)) return;

        final User user = event.getUser();

        if (user.isBot()) return;

        if (this.defaultEmbed == null && this.defaultMessage.isEmpty()) return;

        final MessageChannelUnion channel = event.getChannel();

        if (this.defaultEmbed != null) {
            this.defaultEmbed.sendEmbed(channel, user);

            return;
        }

        channel.sendMessage(this.defaultMessage).queue();
    }

    public @NotNull final String getDescription() {
        return this.description;
    }

    public @NotNull final String getCommand() {
        return this.command;
    }
}