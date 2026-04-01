package com.ryderbelserion.discord.bot.api.objects.commands;

import com.ryderbelserion.discord.api.utils.ConfigUtils;
import com.ryderbelserion.discord.api.utils.StringUtils;
import com.ryderbelserion.discord.bot.api.managers.EmbedManager;
import com.ryderbelserion.discord.bot.api.objects.BeidouEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.CommentedConfigurationNode;

import java.util.ArrayList;
import java.util.List;

public class BeidouCommand {

    private final String command;
    private final String description;
    private final boolean isEnabled;
    private final boolean isSlash;
    private final BeidouEmbed defaultEmbed;
    private final List<BeidouEmbed> extraEmbeds = new ArrayList<>();
    private final String defaultMessage;

    public BeidouCommand(@NotNull final String id, @NotNull final CommentedConfigurationNode configuration, @NotNull final EmbedManager manager) {
        this.command = configuration.node("name").getString("");
        this.isEnabled = configuration.node("enabled").getBoolean(false);
        this.description = configuration.node("description").getString("");
        this.isSlash = configuration.node("is-slash").getBoolean(true);
        this.defaultEmbed = manager.getEmbed(id, configuration.node("embed", "default").getString("")).orElse(null);

        final List<String> extra = ConfigUtils.getStringList(configuration.node("embed", "extra"));

        for (final String line : extra) {
            if (line.isBlank()) continue;

            manager.getEmbed(id, line).ifPresent(this.extraEmbeds::add);
        }

        this.defaultMessage = StringUtils.toString(ConfigUtils.getStringList(configuration.node("message")));
    }

    public void interact(@NotNull final SlashCommandInteractionEvent event) {
        if (!this.isEnabled) return;

        if (this.command.isBlank()) return;

        if (!this.isSlash) return;

        final String name = event.getName();

        if (!name.equalsIgnoreCase(this.command)) return;

        final User user = event.getUser();

        if (user.isBot()) return;

        if (this.defaultEmbed == null && this.defaultMessage.isEmpty()) return;

        if (this.defaultEmbed != null) {
            this.defaultEmbed.sendEmbed(event, user, this.extraEmbeds);

            return;
        }

        event.reply(this.defaultMessage).queue();
    }

    public @NotNull final String getDescription() {
        return this.description;
    }

    public @NotNull final String getCommand() {
        return this.command;
    }

    public final boolean isEnabled() {
        return this.isEnabled;
    }
}