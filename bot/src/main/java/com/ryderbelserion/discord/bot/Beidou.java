package com.ryderbelserion.discord.bot;

import com.ryderbelserion.discord.api.DiscordPlugin;
import com.ryderbelserion.discord.api.options.OptionsManager;
import com.ryderbelserion.discord.api.options.types.EnvOption;
import com.ryderbelserion.discord.bot.commands.owner.AboutCommand;
import com.ryderbelserion.discord.bot.commands.owner.ReloadCommand;
import com.ryderbelserion.discord.bot.configs.ConfigManager;
import com.ryderbelserion.discord.bot.api.environment.enums.Environment;
import com.ryderbelserion.discord.bot.configs.types.BotConfig;
import com.ryderbelserion.discord.bot.guilds.GuildListener;
import com.ryderbelserion.discord.bot.guilds.GuildManager;
import com.ryderbelserion.discord.bot.guilds.features.logging.listeners.MessageListener;
import com.ryderbelserion.discord.bot.guilds.features.threads.ThreadListener;
import com.ryderbelserion.fusion.files.enums.FileAction;
import com.ryderbelserion.fusion.files.enums.FileType;
import com.ryderbelserion.fusion.files.types.configurate.YamlCustomFile;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Beidou extends DiscordPlugin {

    private Environment environment;

    public Beidou(@NotNull final String token, @NotNull final Logger logger, @NotNull final OptionsManager manager) {
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

        manager.getOptionByName("environment").ifPresent(action -> manager.getOption().ifPresent(option -> {
            final EnvOption envOption = (EnvOption) action;

            envOption.getValue(option).ifPresentOrElse(env -> environment = env, () -> environment = Environment.RELEASE);
        }));
    }

    private ConfigManager configManager;
    private GuildManager guildManager;

    @Override
    public void onGuildReady(@NotNull final Guild guild) {
        final String name = Objects.requireNonNull(guild.getOwner()).getEffectiveName();
        final String id = guild.getOwnerId();

        final String guildName = guild.getName();
        final String guildId = guild.getId();

        if (!isWhitelisted(guildId)) {
            leaveGuild(guild, guildId, name, id);

            return;
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

        List.of(
                "cache.json",

                "config.yml",
                "traffic.yml"
        ).forEach(file -> {
            final String extension = file.split("\\.")[1];

            final FileType fileType = switch (extension) {
                case "json" -> FileType.JSON;
                case "yml" -> FileType.YAML;
                default -> throw new IllegalStateException("Unexpected value: " + extension);
            };

            final Path path = directory.resolve(file);

            this.fileManager.extractFile("guilds/%s".formatted(file), path);

            this.fileManager.addFile(path, fileType, consumer -> consumer.addAction(FileAction.ALREADY_EXTRACTED));
        });

        final Path addons = directory.resolve("addons");

        try {
            if (!Files.exists(addons)) {
                Files.createDirectory(addons);
            }
        } catch (final IOException exception) {
            exception.printStackTrace();
        }

        this.guildManager.addGuild(guild, directory);
    }

    @Override
    public void onReady(@NotNull final JDA jda) {
        super.onReady(jda);

        final Path directory = getDirectory();

        List.of(
                "config.yml"
        ).forEach(file -> {
            final String extension = file.split("\\.")[1];

            final FileType fileType = switch (extension) {
                case "json" -> FileType.JSON;
                case "yml" -> FileType.YAML;
                default -> throw new IllegalStateException("Unexpected value: " + extension);
            };

            final Path path = directory.resolve(file);

            this.fileManager.addFile(path, fileType);
        });

        final YamlCustomFile customFile = this.fileManager.getYamlFile(directory.resolve("config.yml")).orElseThrow();

        final BotConfig config = new BotConfig(customFile.getConfiguration());

        if (config.isCustomStatusEnabled()) {
            final Activity customStatus = Activity.customStatus(replacePlaceholder(config.getCustomStatus(), Map.of(
                    "{count}", String.valueOf(jda.getGuilds()
                            .stream()
                            .mapToInt(Guild::getMemberCount)
                            .sum())
            )));

            jda.getPresence().setPresence(customStatus, false);
        }

        List.of(
                // bot creator commands
                new ReloadCommand(this),

                // generic bot information
                new AboutCommand()
        ).forEach(command -> {
            this.commandHandler.addCommand(command);

            addEventListener(command);
        });

        switch (this.environment) {
            case RELEASE -> this.addEventListener(new GuildListener(this), new ThreadListener(this));

            case DEVELOPMENT -> this.addEventListener(new GuildListener(this), new ThreadListener(this), new MessageListener(this));
        }

        this.logger.info("{} is ready!", jda.getSelfUser().getName());
    }

    @Override
    public void onReload(@NotNull final JDA jda) {
        this.configManager.reload();

        final List<Guild> guilds = jda.getGuilds();

        for (final Guild guild : guilds) {
            this.onGuildReady(guild);
        }
    }

    @Override
    public void onStop(@NotNull final JDA jda) {
        this.logger.info("{} is offline!", jda.getSelfUser().getName());
    }

    @Override
    public void init() {
        super.init();

        final Path directory = getDirectory();

        this.fileManager.addFile(directory.resolve("guilds.json"), FileType.JSON)
                .addFile(directory.resolve("config.yml"), FileType.YAML);

        this.configManager = new ConfigManager(this.fileManager, getDirectory());
        this.configManager.init();

        this.guildManager = new GuildManager(this.fileManager, this.logger);
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean isWhitelisted(@NotNull final String guildId) {
        final List<String> cache = this.configManager.getGuildCache().getGuilds();

        return cache.contains(guildId);
    }

    public void leaveGuild(@NotNull final Guild guild, final String guildId, final String username, final String userId) {
        final String guildName = guild.getName();

        this.logger.info("{} ({}) tried adding me to {} ({}) while they are not whitelisted.", username, userId, guildName, guildId);

        guild.leave().queue(_ -> this.logger.info("Successfully left the server {} ({}) owned by {} ({}) due to not being whitelisted", guildName, guildId, username, userId));
    }

    public @NotNull final GuildManager getGuildManager() {
        return this.guildManager;
    }

    public @NotNull final Environment getEnvironment() {
        return environment;
    }
}