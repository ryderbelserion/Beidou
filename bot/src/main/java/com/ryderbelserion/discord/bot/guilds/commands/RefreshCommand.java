package com.ryderbelserion.discord.bot.guilds.commands;

import com.ryderbelserion.discord.api.commands.CommandContext;
import com.ryderbelserion.discord.api.commands.CommandEngine;
import com.ryderbelserion.discord.api.embeds.Embed;
import com.ryderbelserion.discord.api.embeds.EmbedColor;
import com.ryderbelserion.discord.bot.api.objects.BeidouGuild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.jspecify.annotations.NonNull;

public class RefreshCommand extends CommandEngine {

    private final BeidouGuild guild;

    public RefreshCommand(@NonNull final BeidouGuild guild) {
        super("refresh", "Reloads a specific extension");

        this.guild = guild;

        this.addChoice("extension_id", guild.getExtensionManager().getExtensions().keySet().stream().toList());
    }

    @Override
    protected CommandData getCommandData() {
        return Commands.slash(getName(), getDescription()).addOption(OptionType.STRING, "extension_id", "The extension id", false, true);
    }

    @Override
    protected void perform(
            @NonNull final SlashCommandInteractionEvent event,
            @NonNull final CommandContext context
    ) {
        final User user = context.getAuthor();

        if (!this.guild.isOwner(user.getId())) {
            final Embed embed = new Embed()
                    .description("You do not have permission to run this command.")
                    .timestamp()
                    .author(user)
                    .color(EmbedColor.FAIL);

            context.reply(embed.build(), false);

            return;
        }

        final OptionMapping arg1 = context.getOption("extension_id");

        if (arg1 == null) {
            //todo() send message

            return;
        }

        this.guild.getExtensionManager().getExtension(arg1.getAsString()).ifPresent(extension -> {
            if (!extension.isEnabled()) {
                //todo() send message

                return;
            }

            extension.onReload();
        });
    }
}