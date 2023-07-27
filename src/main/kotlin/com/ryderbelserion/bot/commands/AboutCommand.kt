package com.ryderbelserion.bot.commands

import com.ryderbelserion.api.command.CommandContext
import com.ryderbelserion.api.command.CommandEngine
import com.ryderbelserion.api.embeds.Embed
import com.ryderbelserion.api.embeds.EmbedColors

class AboutCommand : CommandEngine("about", "Shows information about the bot.") {

    override fun perform(context: CommandContext) {
        val embed = Embed()
            .description("""
                Hi, My name is **Krul Tepes**!
                I am written by ${context.creator()?.effectiveName} using [Kotlin](https://kotlinlang.org/)
                
                You can find my source code [here](https://github.com/ryderbelserion/Krul)
            """.trimIndent())
            .thumbnail(context.bot(), context.guild())
            .author(context.author(), context.guild())
            .footer("Average Ping: ${context.jda().gatewayPing}", context.guild()?.iconUrl)
            .timestamp()
            .color(EmbedColors.EDIT)
            .fields {
                field(
                    "Other Info",
                    """
                        <:stonks:1134203945917620444> Total Servers: ${context.jda().guilds.size}
                        <:watching:1115489514161455124> Total Members: ${context.jda().guilds.sumOf { it.memberCount }}
                    """.trimIndent(),
                    true
                )
            }
            .build()

        context.reply(embed, true)
    }
}