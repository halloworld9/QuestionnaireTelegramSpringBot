package com.example.questionnaireTelegramSpringBot

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.meta.api.methods.polls.SendPoll
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
                if (editMessageText.text == "Опрос успешно добавлен") {
                    val chatId = update.callbackQuery.message.chatId
                    val time = update.callbackQuery.data.split(" ")[1]
                    startPolling(chatId, time)
                }
            }
        }

    }

    private fun startPolling(chatId: Long, time: String) {
        val chat = chatsContainer.chatIdChatMap[chatId] ?: return

        val startTime = Calendar.getInstance(TimeZone.getTimeZone("Europe/Astrakhan"))
        val minuteAndHour = time.split(":")
        val hour = minuteAndHour[0].toInt()
        val minute = minuteAndHour[1].toInt()
        if (startTime.get(Calendar.HOUR_OF_DAY) * 60 + startTime.get(Calendar.MINUTE) > hour * 60 + minute)
            startTime.add(Calendar.DATE, 1)
        startTime.set(Calendar.HOUR_OF_DAY, hour)
        startTime.set(Calendar.MINUTE, minute)
        startTime.set(Calendar.SECOND, 0)

        val task = object : TimerTask() {
            override fun run() = sendPoll(chatId)
        }
        timer.schedule(task, startTime.time, 1000 * 60 * 60 * 24)
        chat.timeTimerMap[time] = task
    }

    fun sendPoll(chatId: Long) {
        val poll = SendPoll()
        poll.chatId = chatId.toString()
        poll.question = "Кто пойдет сегодня"
        poll.options = listOf("Да", "Нет", "Может быть")
        poll.isAnonymous = false
        execute(poll)
    }
}
