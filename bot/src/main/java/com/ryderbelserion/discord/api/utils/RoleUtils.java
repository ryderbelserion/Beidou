package com.ryderbelserion.discord.api.utils;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import org.jspecify.annotations.NonNull;
import java.util.List;
import java.util.Optional;

public class RoleUtils {

    public static @NonNull Optional<Role> getHighestRoleByColor(@NonNull final Member member) {
        Role foundRole = null;

        for (final Role role : member.getRoles()) {
            if (role.getColor() == null) {
                continue;
            }

            foundRole = role;

            break;
        }

        return Optional.ofNullable(foundRole);
    }

    public static @NonNull Optional<Role> getHighestRole(@NonNull final Member member) {
        final List<Role> roles = member.getRoles();

        return roles.isEmpty() ? Optional.empty() : Optional.of(roles.getFirst());
    }

    public static @NonNull String getRoleName(@NonNull final Role role) {
        return role.getName();
    }
}