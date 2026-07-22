package com.ryderbelserion.discord.bot.api.managers;

import com.ryderbelserion.discord.bot.api.objects.BeidouEmbed;
import com.ryderbelserion.discord.bot.api.objects.BeidouGuild;
import com.ryderbelserion.fusion.files.FileManager;
import com.ryderbelserion.fusion.files.types.configurate.YamlCustomFile;
import net.dv8tion.jda.api.entities.Guild;
import org.jspecify.annotations.NonNull;
import org.spongepowered.configurate.CommentedConfigurationNode;
import java.nio.file.Path;
import java.util.*;

public class EmbedManager {

    private final Map<String, Map<String, BeidouEmbed>> embeds = new HashMap<>();

    public void init(@NonNull final BeidouGuild instance) {
        final Guild guild = instance.getGuild();

        final String id = guild.getId();

        this.embeds.remove(id);

        final FileManager fileManager = instance.getFileManager();
        final Path directory = instance.getDirectory();

        final int depth = instance.getConfigManager().getConfig().getFileConfig().getRecursionDepth();

        final String defaultAvatar = instance.getConfig().getDefaultAvatar();

        for (final Path path : fileManager.getFilesByPath(directory.resolve("embeds"), ".yml", depth)) {
            final Optional<YamlCustomFile> optional = fileManager.getYamlFile(path);

            if (optional.isEmpty()) continue;

            final CommentedConfigurationNode configuration = optional.get().getConfiguration();

            final String fileName = path.getFileName().toString();

            this.embeds.computeIfAbsent(id, _ -> new HashMap<>()).putIfAbsent(fileName, new BeidouEmbed(defaultAvatar,
                    configuration.node("embed")
            ));
        }
    }

    public @NonNull final Optional<BeidouEmbed> getEmbed(@NonNull final String id, @NonNull final String key) {
        return Optional.ofNullable(this.embeds.getOrDefault(id, Map.of()).getOrDefault(key, null));
    }

    public @NonNull final Map<String, BeidouEmbed> getEmbeds(@NonNull final String id) {
        return Collections.unmodifiableMap(this.embeds.getOrDefault(id, Map.of()));
    }
}