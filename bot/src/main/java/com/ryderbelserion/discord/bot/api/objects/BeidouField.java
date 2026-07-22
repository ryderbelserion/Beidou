package com.ryderbelserion.discord.bot.api.objects;

import com.ryderbelserion.discord.api.utils.ConfigUtils;
import com.ryderbelserion.discord.api.utils.StringUtils;
import org.jspecify.annotations.NonNull;
import org.spongepowered.configurate.CommentedConfigurationNode;

public class BeidouField {

    private final String title;
    private final String body;
    private final boolean inline;

    public BeidouField(@NonNull final CommentedConfigurationNode configuration) {
        this.title = configuration.node("title").getString("");
        this.body = StringUtils.toString(ConfigUtils.getStringList(configuration.node("body")));
        this.inline = configuration.node("inline").getBoolean(false);
    }

    public @NonNull final String getTitle() {
        return this.title;
    }

    public @NonNull final String getBody() {
        return this.body;
    }

    public final boolean isInline() {
        return this.inline;
    }
}