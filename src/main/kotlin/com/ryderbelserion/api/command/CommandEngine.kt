package com.ryderbelserion.api.command

import net.dv8tion.jda.api.hooks.ListenerAdapter

abstract class CommandEngine(val name: String, val desc: String) : ListenerAdapter()