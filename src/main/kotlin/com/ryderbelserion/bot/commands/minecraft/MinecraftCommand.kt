package com.ryderbelserion.bot.commands.minecraft

import com.ryderbelserion.vital.commands.CommandContext
import com.ryderbelserion.vital.commands.CommandEngine
import net.dv8tion.jda.api.Permission

class MinecraftCommand : CommandEngine("minecraft", "A set of commands related to Minecraft", Permission.MESSAGE_SEND) {

    override fun perform(context: CommandContext) {
        context.reply("Feck you", false)

        /*val ip = context.getOption("server-ip")?.asString

        if (ip != null) {
            context.reply(ip, false)
        }*/
    }
}