package com.ryderbelserion

import com.ryderbelserion.bot.Beidou
import com.ryderbelserion.api.options.OptionsManager
import com.ryderbelserion.api.options.types.TokenOption
import org.slf4j.LoggerFactory

fun main(args: Array<String>) {
    val options = OptionsManager()

    options.init(args)

    val logger = LoggerFactory.getLogger(Beidou::class.java)

    options.getOptionByName("token").ifPresent({ action ->
        options.getOptionSet().ifPresent { option ->
            val tokenOption: TokenOption = action as TokenOption

            tokenOption.getValue(option).ifPresentOrElse({ token ->
                val bot = Beidou(token, logger)

                bot.init()
            }, {
                logger.warn("Failed to start Discord Bot! Token not found!")
            })
        }
    })
}