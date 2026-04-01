package com.ryderbelserion.discord.bot.api.managers;

import com.ryderbelserion.discord.bot.api.objects.BeidouEmbed;
import com.ryderbelserion.discord.bot.api.objects.BeidouGuild;
import com.ryderbelserion.fusion.files.FileManager;
import com.ryderbelserion.fusion.files.types.configurate.YamlCustomFile;
import net.dv8tion.jda.api.entities.Guild;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.CommentedConfigurationNode;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class EmbedManager {

    private final Map<String, Map<String, BeidouEmbed>> embeds = new HashMap<>();

    public void init(@NotNull final BeidouGuild instance) {
        final Guild guild = instance.getGuild();

        final String id = guild.getId();

        this.embeds.remove(id);

        final FileManager fileManager = instance.getFileManager();
        final Path directory = instance.getDirectory();

        for (final Path path : fileManager.getFilesByPath(directory.resolve("embeds"), ".yml", 1)) {
            final Optional<YamlCustomFile> optional = fileManager.getYamlFile(path);

            if (optional.isEmpty()) continue;

            final CommentedConfigurationNode configuration = optional.get().getConfiguration();

            final String fileName = path.getFileName().toString();

            this.embeds.computeIfAbsent(id, _ -> new HashMap<>()).putIfAbsent(fileName, new BeidouEmbed(
                    configuration,
                    Map.of("{timestamp}", fileName)
            ));
        }
    }

    public @NotNull final Optional<BeidouEmbed> getEmbed(@NotNull final String id, @NotNull final String key) {
        return Optional.ofNullable(this.embeds.getOrDefault(id, Map.of()).getOrDefault(key, null));
    }

    public @NotNull final Map<String, BeidouEmbed> getEmbeds(@NotNull final String id) {
        return Collections.unmodifiableMap(this.embeds.getOrDefault(id, Map.of()));
    }
}