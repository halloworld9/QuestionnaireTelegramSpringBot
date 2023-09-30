package com.example.questionnaireTelegramSpringBot

import jakarta.persistence.*


@Entity
@IdClass(ChatId::class)
class ChatEntity(
    @Id
    val chatId: Long,
    @Id
    val time: String
) {}

