package com.ryderbelserion.discord.bot.commands.owner;

import com.ryderbelserion.discord.api.commands.CommandContext;
import com.ryderbelserion.discord.api.commands.CommandEngine;
import com.ryderbelserion.discord.api.embeds.Embed;
import com.ryderbelserion.discord.api.embeds.EmbedColor;
import com.ryderbelserion.discord.bot.Beidou;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.SelfUser;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.jetbrains.annotations.NotNull;

public class AboutCommand extends CommandEngine {

    private final Beidou instance;

    public AboutCommand(@NotNull final Beidou instance) {
        super("about", "Shows information about the Discord Bot.");

        this.instance = instance;
    }

    @Override
    protected @NotNull final CommandData getCommandData() {
        return Commands.slash(getName(), getDescription());
    }

    @Override
    protected void perform(
            @NotNull final SlashCommandInteractionEvent event,
            @NotNull final CommandContext context
    ) {
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
                            "Statistics",
                            """
                                  <:nice:1431419218351624362> Total Servers: %s
                                  <:worried:1431419202291634258> Total Members: %s
                                  """.formatted(jda.getGuilds().size(), jda.getGuilds()
                                    .stream()
                                    .mapToInt(Guild::getMemberCount)
                                    .sum()),
                            true
                    );

                    consumer.field("Version", "%s".formatted(this.instance.getVersion()));
                });

        final User author = context.getAuthor();
        final Guild guild = context.getGuild();

        if (guild != null) {
            embed.thumbnail(bot, guild).author(author, guild).footer("Average Ping: %s".formatted(jda.getGatewayPing()), this.instance.getIconUrl(guild));
        } else {
            embed.thumbnail(bot).author(author).footer("Average Ping: %s".formatted(jda.getGatewayPing()), this.instance.getIconUrl(bot));
        }

        context.reply(embed.build(), true);
    }
}