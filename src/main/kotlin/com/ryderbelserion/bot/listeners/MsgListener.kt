package com.ryderbelserion.bot.listeners

import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.events.message.MessageDeleteEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class MsgListener : ListenerAdapter() {

    private val messages = hashMapOf<Long, Message>()

    override fun onMessageReceived(event: MessageReceivedEvent) {
        if (event.author.isBot || event.isWebhookMessage) return

        val author = event.author

        val id = event.messageIdLong

        this.messages[id] = event.message
    }

    override fun onMessageDelete(event: MessageDeleteEvent) {
        val channel = event.channel.asTextChannel()

        val data = this.messages[event.messageIdLong]

        event.channel
    }
}