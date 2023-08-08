package com.example.questionnaireTelegramSpringBot

import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText
import org.telegram.telegrambots.meta.api.objects.CallbackQuery

@Component
class CallbackQueryHandler(
    private val addHandler: AddHandler,
    private val hourHandler: HourHandler,
    private val timeHandler: TimeHandler,
    private val listHandler: ListHandler,
    private val startHandler: StartHandler,
    private val removeHandler: RemoveHandler
) {
    fun handle(callbackQuery: CallbackQuery): EditMessageText? {
        val data = callbackQuery.data
        return if (data == "/add")
            addHandler.handle(callbackQuery)
        else if (data.replace(Regex("/hour \\d{1,2}"), "") == "")
            hourHandler.handle(callbackQuery)
        else if (data.replace(Regex("/time \\d{1,2}:\\d{1,2}"), "") == "")
            timeHandler.handle(callbackQuery)
        else if (data == "/list")
            listHandler.handle(callbackQuery)
        else if (data == "/start")
            startHandler.handle(callbackQuery)
        else if (data.replace(Regex("/remove \\d{1,2}:\\d{1,2}"), "") == "")
            removeHandler.handle(callbackQuery)
        else null

    }


}