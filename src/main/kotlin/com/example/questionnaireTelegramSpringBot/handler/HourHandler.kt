package com.example.questionnaireTelegramSpringBot.handler

import com.example.questionnaireTelegramSpringBot.BackButtonCreator
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText
import org.telegram.telegrambots.meta.api.objects.CallbackQuery
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton

@Component
class HourHandler(private val backButtonCreator: BackButtonCreator) {
    fun handle(callbackQuery: CallbackQuery): EditMessageText {
        val hours = callbackQuery.data.split(" ")[1]

        val rows = ArrayList<List<InlineKeyboardButton>>()

        var row = ArrayList<InlineKeyboardButton>()
        for (i in 0..59) {
            if (i % 6 == 0 && i != 0) {
                rows.add(row)
                row = ArrayList()
            }
            val button = InlineKeyboardButton()
            val text = if (i < 10) "0$i" else i.toString()
            button.text = text
            button.callbackData = "/time $hours:$text"
            row.add(button)
        }

        rows.add(row)
        rows.add(listOf(backButtonCreator.createBackButton("/add")))


        val editMessage = EditMessageText()
        editMessage.text = "Выберите минуты"
        editMessage.chatId = callbackQuery.message.chatId.toString()
        editMessage.messageId = callbackQuery.message.messageId
        editMessage.replyMarkup = InlineKeyboardMarkup(rows)
        return editMessage
    }

}