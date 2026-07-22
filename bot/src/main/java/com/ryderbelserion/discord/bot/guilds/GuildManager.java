package com.ryderbelserion.discord.bot.guilds;

import com.ryderbelserion.discord.bot.Beidou;
import com.ryderbelserion.discord.bot.api.objects.BeidouGuild;
import net.dv8tion.jda.api.entities.Guild;
import org.jspecify.annotations.NonNull;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class GuildManager {

    private final Map<String, BeidouGuild> guilds = new HashMap<>();

    private final Beidou instance;

    public GuildManager(@NonNull final Beidou instance) {
        this.instance = instance;
    }

    public void addGuild(@NonNull final Guild guild, @NonNull final Path directory) {
        final String id = guild.getId();

        if (this.guilds.containsKey(id)) {
            this.guilds.get(id).init();

            return;
        }

        final BeidouGuild beidou = new BeidouGuild(this.instance, directory, guild);

        beidou.init();

        this.guilds.putIfAbsent(id, beidou);
    }

    public void removeGuild(@NonNull final String id) {
        this.guilds.remove(id);
    }

    public Optional<BeidouGuild> getGuild(@NonNull final String id) {
        return Optional.ofNullable(this.guilds.get(id));
    }

    public @NonNull final Map<String, BeidouGuild> getGuilds() {
        return Collections.unmodifiableMap(this.guilds);
    }
}