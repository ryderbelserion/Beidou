package com.ryderbelserion.tebex.config;

import com.ryderbelserion.fusion.config.ConfigBuilder;
import com.ryderbelserion.fusion.config.properties.PropertyManager;
import com.ryderbelserion.tebex.config.keys.ConfigKeys;
import org.jspecify.annotations.NonNull;
import org.spongepowered.configurate.loader.HeaderMode;
import org.spongepowered.configurate.yaml.enums.NodeStyle;
import java.nio.file.Path;

public class ConfigManager {

    private final Path path;

    public ConfigManager(@NonNull final Path path) {
        this.path = path;
    }

    private PropertyManager config;

    public void init() {
        this.config = ConfigBuilder.withYamlPath(this.path.resolve("config.yml"))
                .configuration(ConfigKeys.class)
                .withNodeStyle(NodeStyle.BLOCK)
                .withHeaderMode(HeaderMode.PRESERVE)
                .withIndent(1)
                .create();
    }

    public void reload() {
        this.config.reload();
    }

    public @NonNull final PropertyManager getConfig() {
        return this.config;
    }
}