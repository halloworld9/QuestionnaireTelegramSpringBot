package com.example.questionnaireTelegramSpringBot

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.Timer

@Component
class ChatsContainer(
    @Value("\${bot.token}")
    val token: String,
    val timer: Timer,
    private val chatRepository: ChatRepository
    ) {
    private val chatIdChatMap = HashMap<Long, Chat>()

    init {
        val chatEntities = chatRepository.findAll()
        for (i in chatEntities) {
            println(i)
            val chatId = i.chatId
            val time = i.time
            val chat = this[chatId]
            chat.startPolling(time)
        }
    }

    operator fun get(id: Long): Chat {
        var chat = chatIdChatMap[id]
        if (chat == null) {
            chat = Chat(id, token, timer, chatRepository)
            chatIdChatMap[id] = chat
        }
        return chat
    }

}