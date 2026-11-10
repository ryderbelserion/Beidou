package com.ryderbelserion.discord.bot.commands.owner;

import com.ryderbelserion.discord.api.commands.CommandContext;
import com.ryderbelserion.discord.api.commands.CommandEngine;
import com.ryderbelserion.discord.api.embeds.Embed;
import com.ryderbelserion.discord.api.embeds.EmbedColor;
import com.ryderbelserion.discord.bot.Beidou;
import com.ryderbelserion.discord.bot.api.objects.BeidouGuild;
import com.ryderbelserion.discord.bot.guilds.GuildManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.jspecify.annotations.NonNull;
import java.util.ArrayList;
import java.util.List;

public class ReloadCommand extends CommandEngine {

    private final GuildManager guildManager;
    private final Beidou beidou;

    public ReloadCommand(@NonNull final Beidou beidou) {
        super("reload", "Reloads the Discord Bot.");

        this.beidou = beidou;
        this.guildManager = this.beidou.getGuildManager();

        final List<String> guilds = new ArrayList<>();

        for (final BeidouGuild instance : this.guildManager.getGuilds().values()) {
            guilds.add(instance.getGuild().getName());
        }

        this.addChoice("guild_name", guilds);
    }

    @Override
    @NonNull
    public final CommandData getCommandData() {
        return Commands.slash(getName(), getDescription()).addOption(OptionType.STRING, "guild_name", "The name of the guild.", false, true);
    }

    @Override
    protected void perform(
            @NonNull final SlashCommandInteractionEvent event,
            @NonNull final CommandContext context
    ) {
        final User user = context.getAuthor();

        if (context.isCreator(user.getId())) {
            final OptionMapping arg1 = context.getOption("guild_name");

            final JDA jda = context.getJDA();

            if (arg1 != null) {
                final String id = arg1.getAsString();

                this.guildManager.getGuildByName(id).ifPresentOrElse(index -> {
                    this.beidou.onReload(index.getId(), context);
                }, () -> {
                    final Embed embed = new Embed()
                            .description("%s is not a valid guild!".formatted(id))
                            .timestamp()
                            .author(user)
                            .color(EmbedColor.FAIL);

                    context.reply(embed.build(), false);
                });

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