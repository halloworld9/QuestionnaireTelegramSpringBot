package com.example.questionnaireTelegramSpringBot

import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText
import org.telegram.telegrambots.meta.api.objects.CallbackQuery
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton

@Component
class StartHandler {
    fun handle(message: Message): SendMessage {
        val addButton = InlineKeyboardButton("добавить")
        addButton.callbackData = "/add"
        val listButton = InlineKeyboardButton("список")
        listButton.callbackData = "/list"

        val row = listOf(addButton, listButton)

        val sendMessage = SendMessage()
        sendMessage.replyMarkup = InlineKeyboardMarkup(listOf(row))
        sendMessage.text = "Выберите функцию"
        sendMessage.chatId = message.chatId.toString()

        return sendMessage
    }

    fun handle(callbackQuery: CallbackQuery): EditMessageText {
        val addButton = InlineKeyboardButton("добавить")
        addButton.callbackData = "/add"
        val listButton = InlineKeyboardButton("список")
        listButton.callbackData = "/list"

        val row = listOf(addButton, listButton)

        val sendMessage = EditMessageText()
        sendMessage.replyMarkup = InlineKeyboardMarkup(listOf(row))
        sendMessage.text = "Выберите функцию"
        sendMessage.chatId = callbackQuery.message.chatId.toString()
        sendMessage.messageId = callbackQuery.message.messageId
        return sendMessage
    }

}