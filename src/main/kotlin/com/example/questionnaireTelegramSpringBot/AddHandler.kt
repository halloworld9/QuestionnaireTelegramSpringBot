package com.example.questionnaireTelegramSpringBot

import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText
import org.telegram.telegrambots.meta.api.objects.CallbackQuery
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton

@Component
class AddHandler(private val buttonCreator: BackButtonCreator) {
    fun handle(callbackQuery: CallbackQuery): EditMessageText {
        val rows = ArrayList<List<InlineKeyboardButton>>()
        var row = ArrayList<InlineKeyboardButton>()

        for (i in 0..23) {
            if (i % 4 == 0 && i != 0) {
                rows.add(row)
                row = ArrayList()
            }
            val button = InlineKeyboardButton()
            val text = if (i < 10) "0$i" else i.toString()
            button.text = text
            button.callbackData = "/hour $text"
            row.add(button)
        }
        rows.add(row)
        rows.add(listOf(buttonCreator.createBackButton("/start")))

        val editMessageText = EditMessageText()
        editMessageText.chatId = callbackQuery.message.chatId.toString()
        editMessageText.messageId = callbackQuery.message.messageId
        editMessageText.text = "Выберите час"
        editMessageText.replyMarkup = InlineKeyboardMarkup(rows)
        return editMessageText
    }

}