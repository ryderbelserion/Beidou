package com.ryderbelserion.discord.bot;

import com.ryderbelserion.discord.api.DiscordPlugin;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import java.util.List;

public class Beidou extends DiscordPlugin {

    public Beidou(@NotNull final String token, @NotNull final Logger logger) {
        super(List.of(), List.of(), "Beidou", logger, token);
    }

    @Override
    public void onGuildReady(@NotNull final Guild guild) {

    }

    @Override
    public void onReady(@NotNull final JDA jda) {

    }

    @Override
    public void onStop(@NotNull final JDA jda) {

    }
}