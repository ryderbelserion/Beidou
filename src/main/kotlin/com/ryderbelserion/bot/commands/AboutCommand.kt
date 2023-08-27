package com.ryderbelserion.bot.commands

import com.ryderbelserion.api.embeds.Embed
import com.ryderbelserion.api.embeds.EmbedColors
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class AboutCommand : ListenerAdapter() {

    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent): Unit = with(event) {
        if (name != "about") return

        val embed = Embed()
            .description("""
                Hi, My name is **Lapis Lazuli**!
                I am written by ${jda.getUserById("209853986646261762")?.effectiveName} using [Kotlin](https://kotlinlang.org/)
                
                You can find my source code [here](https://github.com/ryderbelserion/Lapis)
            """.trimIndent())
            .thumbnail(jda.selfUser, guild)
            .author(member?.user, guild)
            .footer("Average Ping: ${event.jda.gatewayPing}", guild?.iconUrl)
            .timestamp()
            .color(EmbedColors.EDIT)
            .fields {
                field(
                    "Other Info",
                    """
                        <:stonks:1134203945917620444> Total Servers: ${jda.guilds.size}
                        <:watching:1115489514161455124> Total Members: ${jda.guilds.sumOf { it.memberCount }}
                    """.trimIndent(),
                    true
                )
            }.build()

        event.replyEmbeds(embed).setEphemeral(true).queue()
    }
}