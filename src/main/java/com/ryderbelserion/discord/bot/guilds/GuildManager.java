package com.ryderbelserion.discord.bot.guilds;

import com.ryderbelserion.discord.bot.guilds.objects.BeidouGuild;
import com.ryderbelserion.fusion.files.FileManager;
import org.jetbrains.annotations.NotNull;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class GuildManager {

    private final Map<String, BeidouGuild> guilds = new HashMap<>();

    private final FileManager fileManager;

    public GuildManager(@NotNull final FileManager fileManager) {
        this.fileManager = fileManager;
    }

    public void addGuild(@NotNull final String id, @NotNull final Path directory) {
        this.guilds.putIfAbsent(id, new BeidouGuild(this.fileManager, directory));
    }

    public void removeGuild(@NotNull final String id) {
        this.guilds.remove(id);
    }

    public Optional<BeidouGuild> getGuild(@NotNull final String id) {
        return Optional.ofNullable(this.guilds.get(id));
    }

    public @NotNull final Map<String, BeidouGuild> getGuilds() {
        return Collections.unmodifiableMap(this.guilds);
    }
}