package com.ryderbelserion.discord.bot.guilds.features.traffic.config.types;

import com.ryderbelserion.discord.api.utils.ConfigUtils;
import com.ryderbelserion.discord.bot.api.managers.EmbedManager;
import com.ryderbelserion.discord.bot.guilds.features.traffic.config.interfaces.ITrafficConfig;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.CommentedConfigurationNode;
import java.util.List;

public class EntryConfig implements ITrafficConfig {

    private final EmbedManager embedManager;

    private final boolean isRolePersistEnabled;
    private final List<String> stickyRoles;

    private final boolean isRoleJoinEnabled;
    private final List<String> roles;

    private final List<String> channels;
    private final String embed;

    public EntryConfig(
            @NotNull final CommentedConfigurationNode config,
            @NotNull final EmbedManager embedManager
    ) {
        this.embedManager = embedManager;

        final CommentedConfigurationNode settings = config.node("role-settings");

        this.isRoleJoinEnabled = settings.node("enabled").getBoolean(false);
        this.roles = ConfigUtils.getStringList(settings.node("roles"));

        this.isRolePersistEnabled = settings.node("persist").getBoolean(false);
        this.stickyRoles = ConfigUtils.getStringList(settings.node("sticky-roles"));

        this.channels = ConfigUtils.getStringList(config.node("channels"));
        this.embed = config.node("embed").getString("");
    }

    @Override
    public void send(@NotNull final String id, @NotNull final User user, @NotNull final Guild guild) {
        this.embedManager.getEmbed(id, this.embed).ifPresent(embed -> {
            for (final String channel : this.channels) {
                if (channel.isBlank()) continue;

                final TextChannel union = guild.getTextChannelById(channel);

                if (union == null) continue;

                embed.sendEmbed(union, user);
            }
        });

        if (this.isRoleJoinEnabled) {
            for (final String key : this.roles) {
                if (key.isBlank()) continue;

                final Role role = guild.getRoleById(key);

                if (role == null) continue;

                guild.addRoleToMember(user, role).queue();
            }
        }

        if (this.isRolePersistEnabled) {
            final Member member = guild.getMemberById(user.getId());

            if (member == null) {
                return;
            }

            final List<Role> roles = member.getRoles();

            if (roles.isEmpty()) {
                return;
            }

            for (final Role role : roles) {
                final String role_id = role.getId();

                if (!this.stickyRoles.contains(role_id)) continue;

                // la la, store the sticky role id to their user data, so that when they join. we simply know!
            }
        }
    }
}