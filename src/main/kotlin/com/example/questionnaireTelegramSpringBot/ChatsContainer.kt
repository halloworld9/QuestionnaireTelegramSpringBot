package com.example.questionnaireTelegramSpringBot

import org.springframework.stereotype.Component

@Component
class ChatsContainer {
    val chatIdChatMap = HashMap<Long, Chat>()
}