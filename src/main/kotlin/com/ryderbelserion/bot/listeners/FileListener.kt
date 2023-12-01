package com.ryderbelserion.bot.listeners

import com.ryderbelserion.bot.Beidou
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.utils.FileUpload
import java.io.File

class FileListener(val plugin: Beidou) : ListenerAdapter() {

    override fun onMessageReceived(event: MessageReceivedEvent) {
        val message = event.message

        // Make sure it's not bot.
        if (event.author.isBot) return

        // Make sure attachments are 0.
        if (message.attachments.size <= 0) return

        val attachments = message.attachments.filter { it.fileName.endsWith("mkv") }

        val userFolder = File(plugin.getDataFolder(), "videos/${event.author.idLong}")

        if (!userFolder.exists()) userFolder.mkdirs()

        val files = hashSetOf<FileUpload>()
        val failedFiles = hashSetOf<String>()
        var id: Long? = null

        attachments.forEach { attachment ->
            val file = File(userFolder, attachment.fileName)

            // The file already exists so delete it.
            if (file.exists()) file.delete()

            val newFile = File(userFolder, attachment.fileName.replace("mkv", "mp4"))

            val size = attachment.size / 1024 / 1024

            if (size > 15) {
                // Add the failed file.
                failedFiles.add(attachment.fileName)

                event.message.reply("One of your file(s) seems to be to large to handle").queue {
                    // Bind this message id so we can use later.
                    id = it.idLong
                }

                return
            }

            attachment.proxy.downloadToFile(newFile).whenComplete { _, _ ->
                files.add(FileUpload.fromData(newFile))

                // Filter the files out that exceed the size I can handle.
                val filter = attachments.filter {
                    val filterSize = it.size / 1024 / 1024
                    filterSize > 15
                }

                if (files.size >= filter.size) {
                    val value = if (failedFiles.isNotEmpty()) "I've converted ${files.size} file(s) for you but I did run into an issue. ${failedFiles.size} failed to convert due to size reasons." else "I've converted ${files.size} file(s) for you!"

                    val editOrCreate = if (failedFiles.isNotEmpty()) event.channel.editMessageById(id!!, value) else event.channel.sendMessage(value)

                    editOrCreate.queue {
                        it.replyFiles(files).queue {
                            userFolder.listFiles()?.forEach { check ->
                                check.delete()
                            }

                            // Remove them all.
                            files.clear()
                        }
                    }
                }
            }
        }
    }
}