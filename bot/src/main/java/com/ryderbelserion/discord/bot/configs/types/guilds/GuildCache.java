package com.ryderbelserion.discord.bot.configs.types.guilds;

import com.ryderbelserion.discord.api.utils.ConfigUtils;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.BasicConfigurationNode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GuildCache {

    private final List<String> guilds = new ArrayList<>();

    public void init(@NotNull final BasicConfigurationNode configuration) {
        final List<String> guilds = ConfigUtils.getStringList(configuration.node("whitelist"));

        this.guilds.clear();

        this.guilds.addAll(guilds);
    }

    public @NotNull final List<String> getGuilds() {
        return Collections.unmodifiableList(this.guilds);
    }
}