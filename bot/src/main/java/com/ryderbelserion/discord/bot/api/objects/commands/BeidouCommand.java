package com.ryderbelserion.discord.bot.api.objects.commands;

import com.ryderbelserion.discord.api.commands.CommandContext;
import com.ryderbelserion.discord.api.commands.CommandEngine;
import com.ryderbelserion.discord.api.utils.ConfigUtils;
import com.ryderbelserion.discord.api.utils.StringUtils;
import com.ryderbelserion.discord.bot.api.managers.EmbedManager;
import com.ryderbelserion.discord.bot.api.objects.BeidouEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.CommentedConfigurationNode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BeidouCommand extends CommandEngine {

    private final List<BeidouEmbed> extraEmbeds = new ArrayList<>();
    private final BeidouEmbed defaultEmbed;
    private final String defaultMessage;
    private final boolean isEnabled;
    private final boolean isSlash;

    public BeidouCommand(@NotNull final String id, @NotNull final CommentedConfigurationNode configuration, @NotNull final EmbedManager manager) {
        super(configuration.node("name").getString(""), configuration.node("description").getString(""));

        this.defaultEmbed = manager.getEmbed(id, configuration.node("embed", "default").getString("")).orElse(null);
        this.isEnabled = configuration.node("enabled").getBoolean(false);
        this.isSlash = configuration.node("is-slash").getBoolean(true);

        final List<String> extra = ConfigUtils.getStringList(configuration.node("embed", "extra"));

        for (final String line : extra) {
            if (line.isBlank()) continue;

            manager.getEmbed(id, line).ifPresent(this.extraEmbeds::add);
        }

        this.defaultMessage = StringUtils.toString(ConfigUtils.getStringList(configuration.node("message")));
    }

    @Override
    protected void perform(
            @NotNull final SlashCommandInteractionEvent event,
            @NotNull final CommandContext context
    ) {
        if (!this.isEnabled) return;

        if (this.name.isBlank()) return;

        if (!this.isSlash) return;

        final String name = event.getName();

        if (!name.equalsIgnoreCase(this.name)) return;

        final User user = event.getUser();

        if (user.isBot()) return;

        if (this.defaultEmbed == null && this.defaultMessage.isEmpty()) return;

        if (this.defaultEmbed != null) {
            this.defaultEmbed.sendEmbed(event, user, Map.of(), this.extraEmbeds);

            return;
        }

        event.reply(this.defaultMessage).queue();
    }

    @Override
    public CommandData getCommandData() {
        return Commands.slash(this.name, this.description);
    }

    public final boolean isEnabled() {
        return this.isEnabled;
    }
}