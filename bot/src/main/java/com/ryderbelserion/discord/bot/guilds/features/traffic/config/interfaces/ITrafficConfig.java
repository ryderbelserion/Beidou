package com.ryderbelserion.discord.bot.guilds.features.traffic.config.interfaces;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import org.jetbrains.annotations.NotNull;

public interface ITrafficConfig {

    void send(@NotNull final String id, @NotNull final User user, @NotNull final Guild guild);

}