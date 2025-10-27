package com.ryderbelserion.discord.bot;

import com.ryderbelserion.discord.api.DiscordPlugin;
import com.ryderbelserion.discord.bot.commands.owner.AboutCommand;
import com.ryderbelserion.discord.bot.commands.owner.ReloadCommand;
import com.ryderbelserion.discord.bot.configs.ConfigManager;
import com.ryderbelserion.fusion.files.enums.FileType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

public class Beidou extends DiscordPlugin {

    public Beidou(@NotNull final String token, @NotNull final Logger logger) {
        super(List.of(
                // direct messages
                GatewayIntent.DIRECT_MESSAGE_REACTIONS,
                GatewayIntent.DIRECT_MESSAGE_TYPING,
                GatewayIntent.DIRECT_MESSAGES,

                // universal
                GatewayIntent.MESSAGE_CONTENT,

                // other
                GatewayIntent.SCHEDULED_EVENTS,

                // guilds
                GatewayIntent.GUILD_VOICE_STATES,
                GatewayIntent.GUILD_EXPRESSIONS,
                GatewayIntent.GUILD_PRESENCES,
                GatewayIntent.GUILD_MESSAGES,
                GatewayIntent.GUILD_WEBHOOKS
        ), List.of(
                CacheFlag.VOICE_STATE,
                CacheFlag.EMOJI
        ), "beidou", logger, token);
    }

    private ConfigManager configManager;

    @Override
    public void onGuildReady(@NotNull final Guild guild) {
        final String name = Objects.requireNonNull(guild.getOwner()).getEffectiveName();
        final String id = guild.getOwnerId();

        final String guildName = guild.getName();
        final String guildId = guild.getId();

        final List<String> guilds = this.configManager.getGuildCache().getGuilds();

        final boolean isWhitelisted = guilds.contains(guildId);

        if (!isWhitelisted) {
            this.logger.info("{} ({}) tried adding me to {} ({}) while they are not whitelisted.", name, id, guildName, guildId);

            guild.leave().queue(_ -> this.logger.info("Successfully left the server {} ({}) owned by {} ({}) due to not being whitelisted", guildName, guildId, name, id));
        }

        final Path directory = getGuildDirectory(guildId);

        try {
            if (!Files.exists(directory)) {
                Files.createDirectory(directory);

                this.logger.info("The guild folder for {} ({}) was created.", guildName, guildId);
            }
        } catch (final IOException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void onReady(@NotNull final JDA jda) {
        super.onReady(jda);

        List.of(
                // bot creator commands
                new ReloadCommand(this),

                // generic bot information
                new AboutCommand()
        ).forEach(command -> {
            this.commandHandler.addCommand(command);

            addEventListener(command);
        });

        this.logger.info("{} is ready!", jda.getSelfUser().getName());
    }

    @Override
    public void onReload() {
        this.configManager.reload();
    }

    @Override
    public void onStop(@NotNull final JDA jda) {
        this.logger.info("{} is offline!", jda.getSelfUser().getName());
    }

    @Override
    public void init() {
        super.init();

        final Path directory = getDirectory();

        this.fileManager.addFile(directory.resolve("guilds.json"), FileType.JSON);

        this.configManager = new ConfigManager(this.fileManager, getDirectory());
        this.configManager.init();
    }
}