package com.example.questionnaireTelegramSpringBot

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
        val chat = container.chatIdChatMap[callbackQuery.message.chatId]
        if (chat != null) {
            for (i in markup.keyboard) {
                i.removeIf { it.text == time }
            }
            val task = chat.timeTimerMap[time]
            if (task != null) {
                task.cancel()
                timer.purge()
            }
            chat.timeTimerMap.remove(time)
        }


        val editMessageText = EditMessageText()
        editMessageText.text = "Список\n" +
                "Опрос на $time успешно удален"
        editMessageText.chatId = callbackQuery.message.chatId.toString()
        editMessageText.messageId = callbackQuery.message.messageId
        editMessageText.replyMarkup = markup
        return editMessageText
    }
}