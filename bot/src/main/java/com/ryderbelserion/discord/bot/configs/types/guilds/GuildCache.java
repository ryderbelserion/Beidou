package com.ryderbelserion.discord.bot.configs.types.guilds;

import com.ryderbelserion.discord.api.utils.ConfigUtils;
import com.ryderbelserion.fusion.files.types.configurate.JsonCustomFile;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.BasicConfigurationNode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GuildCache {

    private final List<String> guilds = new ArrayList<>();

    private final JsonCustomFile customFile;

    public GuildCache(@NotNull final JsonCustomFile customFile) {
        this.customFile = customFile;
    }

    public void init() {
        final BasicConfigurationNode configuration = this.customFile.getConfiguration();

        final List<String> guilds = ConfigUtils.getStringList(configuration.node("whitelist"));

        this.guilds.clear();

        this.guilds.addAll(guilds);
    }

    public @NotNull final List<String> getGuilds() {
        return Collections.unmodifiableList(this.guilds);
    }
}