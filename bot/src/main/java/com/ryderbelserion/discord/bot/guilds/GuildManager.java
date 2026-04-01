package com.ryderbelserion.discord.bot.guilds;

import com.ryderbelserion.discord.bot.Beidou;
import com.ryderbelserion.discord.bot.api.objects.BeidouGuild;
import net.dv8tion.jda.api.entities.Guild;
import org.jetbrains.annotations.NotNull;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class GuildManager {

    private final Map<String, BeidouGuild> guilds = new HashMap<>();

    private final Beidou instance;

    public GuildManager(@NotNull final Beidou instance) {
        this.instance = instance;
    }

    public void addGuild(@NotNull final Guild guild, @NotNull final Path directory) {
        final String id = guild.getId();

        if (this.guilds.containsKey(id)) {
            this.guilds.get(id).init();

            return;
        }

        final BeidouGuild beidou = new BeidouGuild(this.instance, directory, guild);

        beidou.init();

        this.guilds.putIfAbsent(id, beidou);
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