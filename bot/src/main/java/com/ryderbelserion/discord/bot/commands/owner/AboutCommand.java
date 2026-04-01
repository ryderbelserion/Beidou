package com.ryderbelserion.discord.bot.commands.owner;

import com.ryderbelserion.discord.api.commands.CommandContext;
import com.ryderbelserion.discord.api.commands.CommandEngine;
import com.ryderbelserion.discord.api.embeds.Embed;
import com.ryderbelserion.discord.api.embeds.EmbedColor;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.SelfUser;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.jetbrains.annotations.NotNull;

public class AboutCommand extends CommandEngine {

    public AboutCommand() {
        super("about", "Shows information about the Discord Bot.");
    }

    @Override
    protected @NotNull final CommandData getCommandData() {
        return Commands.slash(getName(), getDescription());
    }

    @Override
    protected void perform(@NotNull CommandContext context) {
        final JDA jda = context.getJDA();

        final SelfUser bot = context.getBot();

        final Embed embed = new Embed()
                .description("""
                        Hi, My name is **%s**, A pirate of the seven seas!
                        I am written by %s using [Java](https://www.java.com/en/)
                        
                        You can find my source code [here](https://github.com/ryderbelserion/Beidou)
                        """.formatted(bot.getName(), context.getCreator().getAsMention()).trim())
                .timestamp()
                .color(EmbedColor.EDIT)
                .fields(consumer -> {
                    consumer.field(
                            "",
                            """
                                  <:nice:1431419218351624362> Total Servers: %s
                                  <:worried:1431419202291634258> Total Members: %s
                                  """.formatted(jda.getGuilds().size(), jda.getGuilds()
                                    .stream()
                                    .mapToInt(Guild::getMemberCount)
                                    .sum()),
                            true
                    );
                });

        final User author = context.getAuthor();
        final Guild guild = context.getGuild();

        if (guild != null) {
            embed.thumbnail(bot, guild).author(author, guild).footer("Average Ping: %s".formatted(jda.getGatewayPing()), guild.getIconUrl());
        } else {
            embed.thumbnail(bot).author(author).footer("Average Ping: %s".formatted(jda.getGatewayPing()), bot.getAvatarUrl());
        }

        context.reply(embed.build(), true);
    }
}