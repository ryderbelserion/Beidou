package com.ryderbelserion.bot.commands

import com.ryderbelserion.vital.commands.CommandContext
import com.ryderbelserion.vital.commands.CommandEngine
import com.ryderbelserion.vital.embeds.Embed
import com.ryderbelserion.vital.embeds.EmbedColors
import net.dv8tion.jda.api.Permission

class AboutCommand : CommandEngine("about", "Shows information about the Discord Bot.", Permission.MESSAGE_SEND) {

    override fun perform(context: CommandContext) {
        val jda = context.jda()

        val embed = Embed()
            .description("""
                Hi, My name is **Beidou**!
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
        val guild = context.guild()
        val author = context.author()

        if (guild != null) {
            embed.thumbnail(bot, guild).author(author, guild).footer("Average Ping: ${jda.gatewayPing}", guild.iconUrl)
        } else {
            embed.thumbnail(bot).author(author).footer("Average Ping: ${jda.gatewayPing}", bot.avatarUrl)
        }

        context.reply(embed.build(), true)
    }
}