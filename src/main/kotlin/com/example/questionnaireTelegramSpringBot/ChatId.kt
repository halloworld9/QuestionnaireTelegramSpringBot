package com.example.questionnaireTelegramSpringBot

import java.io.Serializable

data class ChatId(val chatId: Long?, val time: String?) : Serializable {
    constructor() : this(null, null)
}

