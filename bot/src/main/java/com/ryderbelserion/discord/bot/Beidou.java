package com.ryderbelserion.discord.bot;

import com.ryderbelserion.discord.api.DiscordPlugin;
import com.ryderbelserion.discord.api.commands.CommandContext;
import com.ryderbelserion.discord.api.commands.CommandHandler;
import com.ryderbelserion.discord.api.embeds.Embed;
import com.ryderbelserion.discord.api.embeds.EmbedColor;
import com.ryderbelserion.discord.api.options.OptionsManager;
import com.ryderbelserion.discord.api.options.types.EnvOption;
import com.ryderbelserion.discord.bot.api.managers.EmbedManager;
import com.ryderbelserion.discord.bot.commands.owner.AboutCommand;
import com.ryderbelserion.discord.bot.commands.owner.ReloadCommand;
import com.ryderbelserion.discord.bot.configs.ConfigManager;
import com.ryderbelserion.discord.bot.api.environment.enums.Environment;
import com.ryderbelserion.discord.bot.configs.types.BotConfig;
import com.ryderbelserion.discord.bot.configs.types.FileConfig;
import com.ryderbelserion.discord.bot.storage.StorageManager;
import com.ryderbelserion.discord.bot.storage.impl.objects.StorageHolder;
import com.ryderbelserion.discord.bot.guilds.GuildListener;
import com.ryderbelserion.discord.bot.guilds.GuildManager;
import com.ryderbelserion.discord.bot.guilds.features.logging.listeners.GuildMessageListener;
import com.ryderbelserion.discord.bot.guilds.features.traffic.TrafficListener;
import com.ryderbelserion.discord.bot.guilds.listeners.GuildSlashListener;
import com.ryderbelserion.discord.bot.guilds.features.threads.ThreadListener;
import com.ryderbelserion.fusion.files.enums.FileAction;
import com.ryderbelserion.fusion.files.enums.FileType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.SelfUser;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.*;

public class Beidou extends DiscordPlugin {

    private final CommandHandler commandHandler;
    private final EmbedManager embedManager;
    private final Timer timer;

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

        this.commandHandler = new CommandHandler();
        this.embedManager = new EmbedManager();
        this.timer = new Timer();
    }

    private StorageHolder storageHolder;
    private ConfigManager configManager;
    private GuildManager guildManager;
    private JDA jda;

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

        final Path commandFolder = directory.resolve("commands");
        final Path embedFolder = directory.resolve("embeds");

        if (!Files.exists(commandFolder) || !Files.exists(embedFolder)) {
            this.fileManager.extractFolder("defaults", directory);
        }

        final Path folder = directory.resolve("defaults");

        if (!Files.exists(embedFolder)) {
            final Path target = folder.resolve("embeds");

            if (Files.exists(target)) {
                try {
                    Files.move(target, embedFolder, StandardCopyOption.REPLACE_EXISTING);
                } catch (final IOException exception) {
                    exception.printStackTrace();
                }
            }
        }

        if (!Files.exists(commandFolder)) {
            final Path target = folder.resolve("commands");

            if (Files.exists(target)) {
                try {
                    Files.move(target, commandFolder, StandardCopyOption.REPLACE_EXISTING);
                } catch (final IOException exception) {
                    exception.printStackTrace();
                }
            }
        }

        try {
            Files.deleteIfExists(folder);
        } catch (final IOException exception) {
            exception.printStackTrace();
        }

        this.fileManager.addFolder(directory.resolve("commands"), FileType.YAML, consumer -> consumer.addAction(FileAction.ALREADY_EXTRACTED));
        this.fileManager.addFolder(directory.resolve("embeds"), FileType.YAML, consumer -> consumer.addAction(FileAction.ALREADY_EXTRACTED));

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
        this.commandHandler.setJda(this.jda = jda);

        final BotConfig config = this.configManager.getConfig();

        if (config.isCustomStatusEnabled()) {
            this.timer.scheduleAtFixedRate(
                    new TimerTask() {
                        @Override
                        public void run() {
                            if (!config.isCustomStatusEnabled()) {
                                cancel();
                            }

                            final Activity customStatus = Activity.customStatus(replacePlaceholder(config.getCustomStatus(), Map.of(
                                    "{count}", String.valueOf(jda.getGuilds()
                                            .stream()
                                            .mapToInt(Guild::getMemberCount)
                                            .sum())
                            )));

                            jda.getPresence().setPresence(customStatus, false);
                        }
                    },
                    0,
                    60000
            );
        }

        List.of(
                // bot creator commands
                new ReloadCommand(this),

                // generic bot information
                new AboutCommand(this)
        ).forEach(command -> {
            this.commandHandler.addCommand(command);

            addEventListener(command);
        });

        switch (this.environment) {
            case RELEASE -> this.addEventListener(
                    new GuildListener(this),
                    new ThreadListener(this),

                    new TrafficListener(this),

                    new GuildSlashListener(this)
            );

            case DEVELOPMENT -> this.addEventListener(
                    new GuildListener(this),
                    new ThreadListener(this),

                    new TrafficListener(this),

                    new GuildSlashListener(this),

                    new GuildMessageListener(this)
            );
        }

        this.logger.info("{} is ready!", jda.getSelfUser().getName());
    }

    @Override
    public void onReload(@NotNull final String id, @NotNull final CommandContext context) {
        final Guild guild = this.jda.getGuildById(id);

        if (guild == null) return;

        onGuildReady(guild);

        context.reply(new Embed()
                .description("We're setting sail! Men, to your posts! A new adventure is about to begin!")
                .author(context.getAuthor())
                .timestamp()
                .footer("Guild: %s".formatted(id), getIconUrl(guild))
                .color(EmbedColor.SUCCESS).build(), true);
    }

    @Override
    public void onReload(@NotNull final JDA jda) {
        this.configManager.reload();

        this.fileManager.setDepth(getRecursionDepth());

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

        this.configManager = new ConfigManager(this.fileManager, directory);
        this.configManager.init();

        this.fileManager.setDepth(getRecursionDepth());

        this.guildManager = new GuildManager(this);

        try {
            this.storageHolder = new StorageManager(this).init();
        } catch (final Exception exception) {
            this.logger.error("Failed to initialize storage impl", exception);
        }
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

    public String getIconUrl(@NotNull final SelfUser user) {
        final String url = user.getAvatarUrl();

        return url == null ? "" : url;
    }

    public String getIconUrl(@NotNull final Guild guild) {
        final String url = guild.getIconUrl();

        return url == null ? "" : url;
    }

    public int getRecursionDepth() {
        final BotConfig config = this.configManager.getConfig();

        final FileConfig fileConfig = config.getFileConfig();

        return fileConfig.getRecursionDepth();
    }

    public @NotNull final CommandHandler getCommandHandler() {
        return this.commandHandler;
    }

    public @NotNull final StorageHolder getStorageHolder() {
        return this.storageHolder;
    }

    public @NotNull final ConfigManager getConfigManager() {
        return this.configManager;
    }

    public @NotNull final GuildManager getGuildManager() {
        return this.guildManager;
    }

    public @NotNull final EmbedManager getEmbedManager() {
        return this.embedManager;
    }

    public @NotNull final Environment getEnvironment() {
        return environment;
    }
}