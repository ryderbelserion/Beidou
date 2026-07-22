package com.ryderbelserion.discord.bot.guilds.features.traffic.config.interfaces;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import org.jspecify.annotations.NonNull;

public interface ITrafficConfig {

    void send(@NonNull final String id, @NonNull final User user, @NonNull final Guild guild);

}