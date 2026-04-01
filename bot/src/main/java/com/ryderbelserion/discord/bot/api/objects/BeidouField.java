package com.ryderbelserion.discord.bot.api.objects;

import com.ryderbelserion.discord.api.utils.ConfigUtils;
import com.ryderbelserion.discord.api.utils.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.CommentedConfigurationNode;
import java.util.Map;

public class BeidouField {

    private final String title;
    private final String body;
    private final boolean inline;

    public BeidouField(@NotNull final CommentedConfigurationNode configuration, @NotNull final Map<String, String> placeholders) {
        this.title = configuration.node("title").getString("");
        this.body = StringUtils.replacePlaceholders(StringUtils.toString(ConfigUtils.getStringList(configuration.node("body"))), placeholders);
        this.inline = configuration.node("inline").getBoolean(false);
    }

    public @NotNull final String getTitle() {
        return this.title;
    }

    public @NotNull final String getBody() {
        return this.body;
    }

    public final boolean isInline() {
        return this.inline;
    }
}