package com.ryderbelserion.bot.listeners

import net.dv8tion.jda.api.events.message.MessageDeleteEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.events.message.MessageUpdateEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class PublicMessageLog : ListenerAdapter() {

    //TODO() Use a proper data file.
    //TODO() Add a request context menu to delete a message.
    //TODO() Add roles that are whitelisted, so they don't get logged.

    private val cache = HashMap<String, String>()

    override fun onMessageReceived(event: MessageReceivedEvent) {
        this.cache.putIfAbsent(event.messageId, event.message.contentRaw)
    }

    override fun onMessageUpdate(event: MessageUpdateEvent) {
        if (!this.cache.containsKey(event.messageId)) {
            this.cache.putIfAbsent(event.messageId, event.message.contentRaw)
            return
        }

        this.cache[event.messageId] = event.message.contentRaw
    }

    override fun onMessageDelete(event: MessageDeleteEvent) {
        if (!this.cache.containsKey(event.messageId)) return

        //TODO() Store the user id then check audit logs to see if it's a ban with deleted messages picked.

        this.cache.remove(event.messageId)
    }
}