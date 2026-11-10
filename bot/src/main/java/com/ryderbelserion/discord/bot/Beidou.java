package com.ryderbelserion.discord.bot;

import com.ryderbelserion.discord.api.DiscordPlugin;
import com.ryderbelserion.discord.api.commands.CommandContext;
import com.ryderbelserion.discord.api.commands.CommandHandler;
import com.ryderbelserion.discord.api.embeds.Embed;
import com.ryderbelserion.discord.api.embeds.EmbedColor;
import com.ryderbelserion.discord.api.options.OptionsManager;
import com.ryderbelserion.discord.api.options.types.EnvOption;
import com.ryderbelserion.discord.bot.api.managers.EmbedManager;
import com.ryderbelserion.discord.bot.commands.misc.FrierenCommand;
import com.ryderbelserion.discord.bot.commands.owner.AboutCommand;
import com.ryderbelserion.discord.bot.commands.owner.ReloadCommand;
import com.ryderbelserion.discord.bot.commands.owner.WhitelistCommand;
import com.ryderbelserion.discord.bot.configs.ConfigManager;
import com.ryderbelserion.discord.bot.api.environment.enums.Environment;
import com.ryderbelserion.discord.bot.configs.types.BotConfig;
import com.ryderbelserion.discord.bot.storage.StorageManager;
import com.ryderbelserion.discord.bot.storage.holder.StorageHolder;
import com.ryderbelserion.discord.bot.guilds.GuildListener;
import com.ryderbelserion.discord.bot.guilds.GuildManager;
import com.ryderbelserion.discord.bot.guilds.features.logging.listeners.GuildMessageListener;
import com.ryderbelserion.discord.bot.guilds.features.traffic.TrafficListener;
import com.ryderbelserion.discord.bot.guilds.listeners.GuildSlashListener;
import com.ryderbelserion.discord.bot.guilds.features.threads.ThreadListener;
import com.ryderbelserion.fusion.addons.ExtensionManager;
import com.ryderbelserion.fusion.files.enums.FileType;
import com.ryderbelserion.fusion.impl.FusionConfig;
import com.ryderbelserion.fusion.impl.keys.RootKeys;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.SelfUser;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.jspecify.annotations.NonNull;
import org.tinylog.Logger;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Beidou extends DiscordPlugin {

    private final String version = "1.3.4";

    private final ScheduledExecutorService executorService;
    private final CommandHandler commandHandler;
    private final EmbedManager embedManager;
    private final FusionConfig fusionConfig;

    private final ExtensionManager extensionManager;
    private Environment environment;

    public Beidou(@NonNull final String token, @NonNull final OptionsManager manager) {
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
                GatewayIntent.GUILD_WEBHOOKS,

                GatewayIntent.GUILD_MEMBERS
        ), List.of(
                CacheFlag.VOICE_STATE,
                CacheFlag.EMOJI
        ), "beidou", token);

        this.fusionConfig = new FusionConfig(this.path);

        manager.getOptionByName("environment").ifPresent(action -> manager.getOption().ifPresent(option -> {
            final EnvOption envOption = (EnvOption) action;

            envOption.getValue(option).ifPresentOrElse(env -> environment = env, () -> environment = Environment.RELEASE);
        }));

        this.extensionManager = new ExtensionManager(getDirectory().resolve("addons"));
        this.commandHandler = new CommandHandler();
        this.embedManager = new EmbedManager();

        this.executorService = Executors.newSingleThreadScheduledExecutor();
    }

    private ConfigManager configManager;
    private GuildManager guildManager;
    private JDA jda;

    private StorageManager storageManager;
    private StorageHolder holder;

    @Override
    public void onGuildReady(@NonNull final Guild guild, final boolean isReload) {
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

                Logger.info("The guild folder for {} ({}) was created.", guildName, guildId);
            }
        } catch (final IOException exception) {
            exception.printStackTrace();
        }

        this.guildManager.addGuild(guild, directory, isReload);
    }

    @Override
    public void onGuildLeave(@NonNull final Guild guild) {
        this.guildManager.removeGuild(guild.getId());
    }

    @Override
    public void onReady(@NonNull final JDA jda) {
        this.commandHandler.setJda(this.jda = jda);

        final BotConfig config = this.configManager.getConfig();

        if (config.isCustomStatusEnabled()) {
            this.executorService.scheduleAtFixedRate(() -> {
                try {
                    if (!config.isCustomStatusEnabled()) {
                        this.executorService.shutdown();
                    }

                    final int size = jda.getGuilds().stream().mapToInt(Guild::getMemberCount).sum();

                    final Activity customStatus = Activity.customStatus(replacePlaceholder(config.getCustomStatus(), Map.of(
                            "{count}", String.valueOf(size)
                    )));

                    jda.getPresence().setPresence(customStatus, false);
                } catch (final Exception exception) {
                    Logger.warn("Failed to complete execution for updating status!", exception);
                }
            }, 0, 1, TimeUnit.MINUTES);
        }

        this.commandHandler.addCommands(List.of(
                // guild whitelist command
                new WhitelistCommand(this),

                // bot creator commands
                new ReloadCommand(this),

                // misc commands
                new FrierenCommand(),

                // generic bot information
                new AboutCommand(this)
        ));

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

        this.extensionManager.init(getRecursionDepth());

        Logger.warn("Successfully loaded {} global addons.", this.extensionManager.getLoadedExtensionCount());

        Logger.info("{} is ready!", jda.getSelfUser().getName());
    }

    @Override
    public void onReload(@NonNull final String id, @NonNull final CommandContext context) {
        final Guild guild = this.jda.getGuildById(id);

        if (guild == null) return;

        onGuildReady(guild, true);

        context.reply(new Embed()
                .description("We're setting sail! Men, to your posts! A new adventure is about to begin!")
                .author(context.getAuthor())
                .timestamp()
                .footer("Guild: %s (Version: %s)".formatted(guild.getName(), this.version), getIconUrl(guild))
                .color(EmbedColor.SUCCESS).build(), true);
    }

    @Override
    public void onReload(@NonNull final JDA jda) {
        this.fusionConfig.reload();

        this.configManager.reload();

        final List<Guild> guilds = jda.getGuilds();

        for (final Guild guild : guilds) {
            this.onGuildReady(guild, true);
        }
    }

    @Override
    public void onStop(@NonNull final JDA jda) {
        Logger.info("{} is offline!", jda.getSelfUser().getName());

        //final Path directory = getDirectory();

        //final Path folder = directory.resolve("logs");

        //this.fileManager.compressFile(folder.resolve("latest.log"), folder);
    }

    @Override
    public void init() {
        this.fusionConfig.build();

        super.init();

        final Path directory = getDirectory();

        this.fileManager.addFile(directory.resolve("config.yml"), FileType.YAML);

        this.configManager = new ConfigManager(this.fileManager, directory);
        this.configManager.init();

        this.guildManager = new GuildManager(this);

        this.storageManager = new StorageManager(this);

        try {
            this.holder = this.storageManager.init();
        } catch (final Exception exception) {
            Logger.error("Failed to initialize storage impl (Reason: {})", exception.getMessage());

            System.exit(1);
        }
    }

    public boolean isWhitelisted(@NonNull final String guildId) {
        return this.holder.hasGuild(guildId);
    }

    public void leaveGuild(@NonNull final Guild guild, final String guildId, final String username, final String userId) {
        final String guildName = guild.getName();

        Logger.info("{} ({}) tried adding me to {} ({}) while they are not whitelisted.", username, userId, guildName, guildId);

        guild.leave().queue(_ -> Logger.info("Successfully left the server {} ({}) owned by {} ({}) due to not being whitelisted", guildName, guildId, username, userId));
    }

    public String getIconUrl(@NonNull final SelfUser user) {
        final String url = user.getAvatarUrl();

        return url == null ? "" : url;
    }

    public String getIconUrl(@NonNull final Guild guild) {
        final String url = guild.getIconUrl();

        return url == null ? "" : url;
    }

    public int getRecursionDepth() {
        return this.fusionConfig.getConfig().getProperty(RootKeys.recursion_depth);
    }

    public @NonNull final CommandHandler getCommandHandler() {
        return this.commandHandler;
    }

    public @NonNull final StorageHolder getStorageHolder() {
        return this.holder;
    }

    public @NonNull final ConfigManager getConfigManager() {
        return this.configManager;
    }

    public @NonNull final GuildManager getGuildManager() {
        return this.guildManager;
    }

    public @NonNull final EmbedManager getEmbedManager() {
        return this.embedManager;
    }

    public @NonNull final Environment getEnvironment() {
        return environment;
    }

    public @NonNull final String getVersion() {
        return this.version;
    }
}