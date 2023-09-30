package com.example.questionnaireTelegramSpringBot.handler

import com.example.questionnaireTelegramSpringBot.BackButtonCreator
import com.example.questionnaireTelegramSpringBot.ChatsContainer
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText
import org.telegram.telegrambots.meta.api.objects.CallbackQuery
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton

@Component
final class ListHandler(
    private val chatsContainer: ChatsContainer,
    private val buttonCreator: BackButtonCreator
) {

    fun handle(callbackQuery: CallbackQuery): EditMessageText {
        val chatId = callbackQuery.message.chatId
        val chat = chatsContainer[chatId]
        val rows = ArrayList<List<InlineKeyboardButton>>()
        var row = ArrayList<InlineKeyboardButton>()
        for ((index, i) in chat.timeTimerMap.keys.asSequence().sorted().withIndex()) {
            if (index % 4 == 0 && index != 0) {
                rows.add(row)
                row = ArrayList()
            }
            val button = InlineKeyboardButton()
            button.text = i
            button.callbackData = "/remove $i"
            row.add(button)
        }
        rows.add(row)

        rows.add(listOf(buttonCreator.createBackButton("/start")))

        val editMessage = EditMessageText()
        editMessage.text = "Список"
        editMessage.chatId = chatId.toString()
        editMessage.messageId = callbackQuery.message.messageId
        editMessage.replyMarkup = InlineKeyboardMarkup(rows)
        return editMessage
    }
}