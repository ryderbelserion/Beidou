package com.ryderbelserion.discord.bot.commands.owner;

import com.ryderbelserion.discord.api.commands.CommandContext;
import com.ryderbelserion.discord.api.commands.CommandEngine;
import com.ryderbelserion.discord.api.embeds.Embed;
import com.ryderbelserion.discord.api.embeds.EmbedColor;
import com.ryderbelserion.discord.bot.Beidou;
import net.dv8tion.jda.api.entities.User;
import org.jetbrains.annotations.NotNull;

public class ReloadCommand extends CommandEngine {

    private final Beidou beidou;

    public ReloadCommand(@NotNull final Beidou beidou) {
        super("reload", "Reloads the Discord Bot.");

        this.beidou = beidou;
    }

    @Override
    protected void perform(@NotNull final CommandContext context) {
        final User user = context.getAuthor();

        if (context.isCreator(user.getId())) {
            this.beidou.onReload(context.getJDA());

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