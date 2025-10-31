package com.ryderbelserion.discord.bot.guilds;

import com.ryderbelserion.discord.bot.guilds.objects.BeidouGuild;
import com.ryderbelserion.fusion.files.FileManager;
import net.dv8tion.jda.api.entities.Guild;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class GuildManager {

    private final Map<String, BeidouGuild> guilds = new HashMap<>();

    private final FileManager fileManager;
    private final Logger logger;

    public GuildManager(@NotNull final FileManager fileManager, @NotNull final Logger logger) {
        this.fileManager = fileManager;
        this.logger = logger;
    }

    public void addGuild(@NotNull final Guild guild, @NotNull final Path directory) {
        final String id = guild.getId();

        if (this.guilds.containsKey(id)) {
            this.guilds.get(id).init(this.fileManager, this.logger, directory, guild);

            return;
        }

        this.guilds.putIfAbsent(id, new BeidouGuild(this.fileManager, this.logger, directory, guild));
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