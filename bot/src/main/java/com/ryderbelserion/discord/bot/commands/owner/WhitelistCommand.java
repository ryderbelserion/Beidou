package com.ryderbelserion.discord.bot.commands.owner;

import com.ryderbelserion.discord.api.commands.CommandContext;
import com.ryderbelserion.discord.api.commands.CommandEngine;
import com.ryderbelserion.discord.api.embeds.Embed;
import com.ryderbelserion.discord.api.embeds.EmbedColor;
import com.ryderbelserion.discord.bot.Beidou;
import com.ryderbelserion.discord.bot.storage.holder.StorageHolder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.jspecify.annotations.NonNull;
import java.util.List;

public class WhitelistCommand extends CommandEngine {

    private final StorageHolder holder;

    public WhitelistCommand(@NonNull final Beidou beidou) {
        super("whitelist", "Allows a guild to use the discord bot!");

        this.holder = beidou.getStorageHolder();

        this.addChoice("whitelisted", List.of("true", "false"));

        this.addChoice("guild_id", List.of());
    }

    @Override
    @NonNull
    public final CommandData getCommandData() {
        return Commands.slash(getName(), getDescription())
                .addOption(OptionType.STRING, "whitelisted", "true or false", true, true)
                .addOption(OptionType.STRING, "guild_id", "guild id", true, true);
    }

    @Override
    protected void perform(
            @NonNull final SlashCommandInteractionEvent event,
            @NonNull final CommandContext context
    ) {
        final User user = context.getAuthor();
        final boolean isCreator = context.isCreator(user.getId());

        if (!isCreator) {
            final Embed embed = new Embed()
                    .description("You do not have permission to run this command.")
                    .timestamp()
                    .author(user)
                    .color(EmbedColor.FAIL);

            context.reply(embed.build(), false);

            return;
        }

        final OptionMapping arg1 = context.getOption("whitelisted");

        if (arg1 == null) {
            //todo() message

            return;
        }

        final OptionMapping arg2 = context.getOption("guild_id");

        if (arg2 == null) {
            //todo() message

            return;
        }

        final String text = arg2.getAsString();

        switch (arg1.getAsString().toLowerCase()) {
            case "true" -> {
                if (this.holder.hasGuild(text)) {
                    final Embed embed = new Embed()
                            .description("Guild %s is already whitelisted!".formatted(text))
                            .author(user)
                            .timestamp()
                            .color(EmbedColor.SUCCESS);

                    context.reply(embed.build(), true);

                    return;
                }

                this.holder.addGuild(text);

                final Embed embed = new Embed()
                        .description("Guild %s is whitelisted!".formatted(text))
                        .author(user)
                        .timestamp()
                        .color(EmbedColor.SUCCESS);

                context.reply(embed.build(), true);
            }

            case "false" -> {
                if (!this.holder.hasGuild(text)) {
                    final Embed embed = new Embed()
                            .description("Guild %s is not whitelisted!".formatted(text))
                            .author(user)
                            .timestamp()
                            .color(EmbedColor.SUCCESS);

                    context.reply(embed.build(), true);

                    return;
                }

                this.holder.removeGuild(text);

                final Embed embed = new Embed()
                        .description("Guild %s is not whitelisted!".formatted(text))
                        .author(user)
                        .timestamp()
                        .color(EmbedColor.SUCCESS);

                context.reply(embed.build(), true);
            }
        }
    }
}