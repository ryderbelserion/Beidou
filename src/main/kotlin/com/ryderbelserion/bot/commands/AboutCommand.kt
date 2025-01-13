package com.ryderbelserion.bot.commands

import com.ryderbelserion.api.commands.CommandContext
import com.ryderbelserion.api.commands.CommandEngine
import com.ryderbelserion.api.embeds.Embed
import com.ryderbelserion.api.embeds.EmbedColors
import net.dv8tion.jda.api.Permission

public class AboutCommand : CommandEngine("about", "Shows information about the Discord Bot.", Permission.MESSAGE_SEND) {

    override fun perform(context: CommandContext) {
        val jda = context.jda()

        val embed = Embed()
            .description("""
                Hi, My name is **${context.bot().name}**, A pirate of the seven seas!
                I am written by ${context.creator()?.asMention} using [Kotlin](https://kotlinlang.org/)
                
                You can find my source code [here](https://github.com/ryderbelserion/Beidou)
            """.trimIndent())
            .timestamp()
            .color(EmbedColors.EDIT)
            .fields {
                field(
                    "",
                    """
                        <:stonks:1134203945917620444> Total Servers: ${jda.guilds.size}
                        <:watching:1115489514161455124> Total Members: ${jda.guilds.sumOf { it.memberCount }}
                    """.trimIndent(),
                    true
                )
            }

        val bot = context.bot()
        val author = context.author()

        if (context.guild() != null) {
            val guild = context.guild()

            embed.thumbnail(bot, guild).author(author, guild).footer("Average Ping: ${jda.gatewayPing}", guild?.iconUrl)
        } else {
            embed.thumbnail(bot).author(author).footer("Average Ping: ${jda.gatewayPing}", bot.avatarUrl)
        }

        context.reply(embed.build(), true)
    }
}