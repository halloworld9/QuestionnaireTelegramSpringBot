package com.example.questionnaireTelegramSpringBot

import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton

@Component
class BackButtonCreator {
    fun createBackButton(callback: String): InlineKeyboardButton {
        val button = InlineKeyboardButton()
        button.text = "Назад"
        button.callbackData = callback
        return button
    }
}
