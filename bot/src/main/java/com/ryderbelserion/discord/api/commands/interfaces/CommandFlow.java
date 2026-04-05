package com.ryderbelserion.discord.api.commands.interfaces;

import com.ryderbelserion.discord.api.commands.CommandEngine;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public interface CommandFlow {

    void addCommands(@NotNull final List<CommandEngine> commands);

    void addStaticGuildCommands(@NotNull final Guild guild, @NotNull final List<CommandEngine> commands);

    void addGuildCommands(@NotNull final Guild guild, @NotNull final List<CommandData> commands);

    void purgeGuildCommands(@NotNull final Guild guild);

    void purgeCommands();
}