package com.ryderbelserion.bot.commands

import com.ryderbelserion.api.builders.commands.CommandContext
import com.ryderbelserion.api.builders.commands.CommandEngine
import com.ryderbelserion.api.builders.embeds.Embed
import com.ryderbelserion.api.builders.embeds.EmbedColors
import net.dv8tion.jda.api.Permission

class AboutCommand : CommandEngine("about", "Shows information about the Discord Bot.", Permission.MESSAGE_SEND) {

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
                        <:nice:1431419218351624362> Total Servers: ${jda.guilds.size}
                        <:worried:1431419202291634258> Total Members: ${jda.guilds.sumOf { it.memberCount }}
                    """.trimIndent(),
                    true
                )
            }

        val bot = context.bot()
        val author = context.author()
        val guild = context.guild()

        if (guild != null) {
            embed.thumbnail(bot, guild).author(author, guild).footer("Average Ping: ${jda.gatewayPing}", guild?.iconUrl)
        } else {
            embed.thumbnail(bot).author(author).footer("Average Ping: ${jda.gatewayPing}", bot.avatarUrl)
        }

        context.reply(embed.build(), true)
    }
}