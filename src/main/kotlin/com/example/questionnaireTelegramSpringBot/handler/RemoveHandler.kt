package com.example.questionnaireTelegramSpringBot.handler

import com.example.questionnaireTelegramSpringBot.ChatsContainer
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText
import org.telegram.telegrambots.meta.api.objects.CallbackQuery
import java.util.*

@Component
class RemoveHandler(
    private val container: ChatsContainer,
    private val timer: Timer
) {

    fun handle(callbackQuery: CallbackQuery): EditMessageText {
        val time = callbackQuery.data.split(" ")[1]
        val markup = callbackQuery.message.replyMarkup
        val chat = container[callbackQuery.message.chatId]
        for (i in markup.keyboard) {
            i.removeIf { it.text == time }
        }
        chat.stopPoll(time)


        val editMessageText = EditMessageText()
        editMessageText.text = "Список\n" +
                "Опрос на $time успешно удален"
        editMessageText.chatId = callbackQuery.message.chatId.toString()
        editMessageText.messageId = callbackQuery.message.messageId
        editMessageText.replyMarkup = markup
        return editMessageText
    }
}