package com.ryderbelserion.discord.api.commands.interfaces;

import com.ryderbelserion.discord.api.commands.CommandEngine;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.jspecify.annotations.NonNull;
import java.util.List;

public interface CommandFlow {

    void addCommands(@NonNull final List<CommandEngine> commands);

    void addStaticGuildCommands(@NonNull final Guild guild, @NonNull final List<CommandEngine> commands);

    void addGuildCommands(@NonNull final Guild guild, @NonNull final List<CommandData> commands);

    void purgeGuildCommands(@NonNull final Guild guild);

    void purgeCommands();
}