package com.ryderbelserion.discord.bot.api.environment.interfaces;

import com.ryderbelserion.discord.bot.api.environment.enums.Environment;

public interface IEnvironmentManager {

    void enable();

    void disable();

    Environment getEnvironment();

}