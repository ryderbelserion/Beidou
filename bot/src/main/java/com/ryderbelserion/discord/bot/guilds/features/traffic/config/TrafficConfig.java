package com.ryderbelserion.discord.bot.guilds.features.traffic.config;

import com.ryderbelserion.discord.bot.api.managers.EmbedManager;
import com.ryderbelserion.discord.bot.guilds.features.traffic.config.interfaces.ITrafficConfig;
import com.ryderbelserion.discord.bot.guilds.features.traffic.config.types.EntryConfig;
import com.ryderbelserion.discord.bot.guilds.features.traffic.config.types.ExitConfig;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.CommentedConfigurationNode;

public class TrafficConfig {

    private final ITrafficConfig entryConfig;
    private final ITrafficConfig exitConfig;

    public TrafficConfig(
            @NotNull final CommentedConfigurationNode config,
            @NotNull final EmbedManager embedManager
    ) {
        this.entryConfig = new EntryConfig(config.node("entry-module"), embedManager);
        this.exitConfig = new ExitConfig(config.node("exit-module"), embedManager);
    }

    public void send(
            @NotNull final String id,
            @NotNull final String type,
            @NotNull final User user,
            @NotNull final Guild guild
    ) {
        switch (type) {
            case "entry" -> this.entryConfig.send(id, user, guild);
            case "exit" -> this.exitConfig.send(id, user, guild);
        }
    }
}