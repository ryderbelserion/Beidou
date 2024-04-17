package com.ryderbelserion.bot.commands

import com.ryderbelserion.vital.commands.CommandContext
import com.ryderbelserion.vital.commands.CommandEngine
import com.ryderbelserion.vital.embeds.Embed
import com.ryderbelserion.vital.embeds.EmbedColors
import net.dv8tion.jda.api.Permission

class AboutCommand : CommandEngine("about", "Shows information about the Discord Bot.", Permission.MESSAGE_SEND) {

    override fun perform(context: CommandContext) {
        val embed = Embed()
            .description("""
                Hi, My name is **Beidou**!
                I am written by ${context.creator()?.asMention} using [Kotlin](https://kotlinlang.org/)
                
                You can find my source code [here](https://github.com/ryderbelserion/Beidou)
            """.trimIndent())
            .thumbnail(context.bot(), context.guild())
            .author(context.author(), context.guild())
            .footer("Average Ping: ${context.jda().gatewayPing}", context.guild()?.iconUrl)
            .timestamp()
            .color(EmbedColors.EDIT)
            .fields {
                field(
                    "",
                    """
                        <:stonks:1134203945917620444> Total Servers: ${context.jda().guilds.size}
                        <:watching:1115489514161455124> Total Members: ${context.jda().guilds.sumOf { it.memberCount }}
                    """.trimIndent(),
                    true
                )
            }.build()

        context.reply(embed, true)
    }
}