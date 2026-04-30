package com.ryderbelserion.discord;

import com.ryderbelserion.discord.api.options.types.TokenOption;
import com.ryderbelserion.discord.bot.Beidou;
import com.ryderbelserion.discord.api.options.OptionsManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    static void main(String[] args) {
        final OptionsManager manager = new OptionsManager();

        manager.init(args);

        final Logger logger = LoggerFactory.getLogger(Beidou.class);

        manager.getOptionByName("token").ifPresent(action -> {
            manager.getOption().ifPresent(option -> {
                final TokenOption tokenOption = (TokenOption) action;

                tokenOption.getValue(option).ifPresentOrElse(token -> new Beidou(token, logger, manager).init(), () -> logger.warn("Failed to start Discord Bot, Token not found!"));
            });
        });
    }
}