package com.ryderbelserion.discord.bot.guilds.features.traffic.config;

import com.ryderbelserion.discord.api.utils.ConfigUtils;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.CommentedConfigurationNode;
import java.util.List;

public class TrafficConfig {

    private boolean isRoleJoinEnabled;
    private List<String> roles;

    public TrafficConfig(@NotNull final CommentedConfigurationNode config) {
        init(config);
    }

    public void init(@NotNull final CommentedConfigurationNode config) {
        this.isRoleJoinEnabled = config.node("join-role", "enabled").getBoolean(false);
        this.roles = ConfigUtils.getStringList(config.node("join-role", "roles"));
    }

    public @NotNull final List<String> getRoles() {
        return this.roles;
    }

    public final boolean isRoleJoinEnabled() {
        return this.isRoleJoinEnabled;
    }
}