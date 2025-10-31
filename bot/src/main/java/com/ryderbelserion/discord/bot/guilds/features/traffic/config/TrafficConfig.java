package com.ryderbelserion.discord.bot.guilds.features.traffic.config;

import com.ryderbelserion.discord.api.utils.ConfigUtils;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.CommentedConfigurationNode;
import java.util.List;

public class TrafficConfig {

    private boolean giveRoleOnJoin;
    private List<String> joinRoles;

    public TrafficConfig(@NotNull final CommentedConfigurationNode config) {
        init(config);
    }

    public void init(@NotNull final CommentedConfigurationNode config) {
        this.giveRoleOnJoin = config.node("join-role", "enabled").getBoolean(false);
        this.joinRoles = ConfigUtils.getStringList(config.node("join-role", "roles"));
    }

    public @NotNull final List<String> getJoinRoles() {
        return this.joinRoles;
    }

    public final boolean isGiveRoleOnJoin() {
        return this.giveRoleOnJoin;
    }
}