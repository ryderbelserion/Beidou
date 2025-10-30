package com.ryderbelserion.api.util

import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.Role

object RoleUtil {

    fun getRoleName(role: Role?): String {
        return role?.name ?: ""
    }

    fun getHighestRole(member: Member): Role? {
        return if (member.roles.isEmpty()) null else member.roles[0]
    }

    fun getHighestRoleWithColor(member: Member): Role? {
        var foundRole: Role? = null

        for (role in member.roles) {
            if (role.color == null) {
                continue
            }

            foundRole = role

            break
        }

        return foundRole
    }
}