package com.example.questionnaireTelegramSpringBot


import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.web.client.RestTemplate
import org.telegram.telegrambots.meta.api.methods.polls.SendPoll
import java.util.*

class Chat(
    private val chatId: Long, val token: String,
    private val timer: Timer,
    private val chatRepository: ChatRepository
) {
    val timeTimerMap = HashMap<String, TimerTask>()
    fun addPoll(time: String) {
        startPolling(time)
        chatRepository.save(ChatEntity(chatId, time))
    }

    fun startPolling(time: String) {
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
            override fun run() {
                val restTemplate = RestTemplate()
                restTemplate.postForLocation(
                    "https://api.telegram.org/bot$token/sendPoll",
                    createPoll())
            }

        }
        timeTimerMap[time] = task
        timer.schedule(task, startTime.time, 1000 * 60 * 60 * 24)
    }
    fun stopPoll(time: String) {
        val task = timeTimerMap.remove(time)
        if (task != null) {
            task.cancel()
            timer.purge()
            chatRepository.delete(ChatEntity(chatId, time))
        }
    }

    private fun createPoll(): SendPoll {
        val poll = SendPoll()
        poll.chatId = chatId.toString()
        poll.question = "Кто пойдет сегодня"
        poll.options = listOf("Да", "Нет", "Может быть")
        poll.isAnonymous = false
        return poll
    }
}