package com.ryderbelserion.discord.bot;

import com.ryderbelserion.discord.api.DiscordPlugin;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import java.util.List;

public class Beidou extends DiscordPlugin {

    public Beidou(@NotNull final String token, @NotNull final Logger logger) {
        super(List.of(
                // direct messages
                GatewayIntent.DIRECT_MESSAGE_REACTIONS,
                GatewayIntent.DIRECT_MESSAGE_TYPING,
                GatewayIntent.DIRECT_MESSAGES,

                // universal
                GatewayIntent.MESSAGE_CONTENT,

                // other
                GatewayIntent.SCHEDULED_EVENTS,

                // guilds
                GatewayIntent.GUILD_VOICE_STATES,
                GatewayIntent.GUILD_EXPRESSIONS,
                GatewayIntent.GUILD_PRESENCES,
                GatewayIntent.GUILD_MESSAGES,
                GatewayIntent.GUILD_WEBHOOKS
        ), List.of(
                CacheFlag.VOICE_STATE,
                CacheFlag.EMOJI
        ), "beidou", logger, token);
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