package com.ryderbelserion.discord.bot.commands.owner;

import com.ryderbelserion.discord.api.commands.CommandContext;
import com.ryderbelserion.discord.api.commands.CommandEngine;
import com.ryderbelserion.discord.api.embeds.Embed;
import com.ryderbelserion.discord.api.embeds.EmbedColor;
import com.ryderbelserion.discord.bot.Beidou;
import com.ryderbelserion.discord.bot.guilds.GuildManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.jetbrains.annotations.NotNull;

public class ReloadCommand extends CommandEngine {

    private final Beidou beidou;

    public ReloadCommand(@NotNull final Beidou beidou) {
        super("reload", "Reloads the Discord Bot.");

        this.beidou = beidou;

        final GuildManager guildManager = this.beidou.getGuildManager();

        this.addChoice("guild_id", guildManager.getGuilds().keySet().stream().toList());
    }

    @Override
    protected @NotNull final CommandData getCommandData() {
        return Commands.slash(getName(), getDescription()).addOption(OptionType.STRING, "guild_id", "The guild id", false, true);
    }

    @Override
    protected void perform(
            @NotNull final SlashCommandInteractionEvent event,
            @NotNull final CommandContext context
    ) {
        final User user = context.getAuthor();

        if (context.isCreator(user.getId())) {
            final OptionMapping arg1 = context.getOption("guild_id");

            final JDA jda = context.getJDA();

            if (arg1 != null) {
                final String id = arg1.getAsString();

                this.beidou.onReload(id, context);

                return;
            }

            this.beidou.onReload(jda);

            final Embed embed = new Embed()
                    .description("We're setting sail! Men, to your posts! A new adventure is about to begin!")
                    .author(user)
                    .timestamp()
                    .color(EmbedColor.SUCCESS);

            context.reply(embed.build(), true);

            return;
        }

        final Embed embed = new Embed()
                .description("You do not have permission to run this command.")
                .timestamp()
                .author(user)
                .color(EmbedColor.FAIL);

        context.reply(embed.build(), false);
    }
}