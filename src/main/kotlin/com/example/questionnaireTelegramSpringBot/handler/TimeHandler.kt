package com.example.questionnaireTelegramSpringBot.handler

import com.example.questionnaireTelegramSpringBot.BackButtonCreator
import com.example.questionnaireTelegramSpringBot.ChatsContainer
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText
import org.telegram.telegrambots.meta.api.objects.CallbackQuery
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup

@Component
class TimeHandler(
    private val backButtonCreator: BackButtonCreator,
    private val chatsContainer: ChatsContainer
) {

    fun handle(callbackQuery: CallbackQuery): EditMessageText {
        val chatId = callbackQuery.message.chatId
        var chat = chatsContainer[chatId]
        var text = "Опрос успешно добавлен"
        val time = callbackQuery.data.split(" ")[1]
        if (chat.timeTimerMap[time] != null) {
                text = "Опрос на такое время уже существует"
        } else {
            chat.addPoll(time)
        }

        val editMessageText = EditMessageText()
        editMessageText.chatId = chatId.toString()
        editMessageText.messageId = callbackQuery.message.messageId
        editMessageText.text = text
        val markup = InlineKeyboardMarkup()
        markup.keyboard = listOf(listOf(backButtonCreator.createBackButton("/start")))
        editMessageText.replyMarkup = markup
        return editMessageText
    }
}