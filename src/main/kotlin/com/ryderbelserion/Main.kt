package com.ryderbelserion

import com.ryderbelserion.bot.Beidou
import net.kyori.adventure.text.logger.slf4j.ComponentLogger
import net.kyori.adventure.text.minimessage.MiniMessage

public fun main() {
    //Beidou().init()

    ComponentLogger.logger("Beidou").warn(MiniMessage.miniMessage().deserialize("<red>This is a message!</red>"))

}