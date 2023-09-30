package com.example.questionnaireTelegramSpringBot

import com.example.questionnaireTelegramSpringBot.handler.CallbackQueryHandler
import com.example.questionnaireTelegramSpringBot.handler.StartHandler
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText
import org.telegram.telegrambots.meta.api.objects.Update
import java.util.*

@Component
final class QuestionnaireBot(
    telegramBotsApi: TelegramBotsApi,
    @Value("\${bot.username}") val botName: String,
    @Value("\${bot.token}") val token: String,
    private val startHandler: StartHandler,
    private val callbackQueryHandler: CallbackQueryHandler,
    private val chatsContainer: ChatsContainer,
    private val timer: Timer
) : TelegramLongPollingBot(token) {

    init {
        telegramBotsApi.registerBot(this)
    }

    override fun getBotUsername(): String {
        return botName
    }

    override fun onUpdateReceived(update: Update) {
        if (update.hasMessage() && update.message.text.trim() == "/start") {
            execute(startHandler.handle(update.message))
        } else if (update.hasCallbackQuery()) {
            val editMessageText: EditMessageText? = callbackQueryHandler.handle(update.callbackQuery)
            if (editMessageText != null) {
                execute(editMessageText)
            }
        }

    }
}
