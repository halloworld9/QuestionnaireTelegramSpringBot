package com.example.questionnaireTelegramSpringBot

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession
import java.util.*

@Configuration
class Config {

    @Bean
    fun createTelegramApi() : TelegramBotsApi {
        return TelegramBotsApi(DefaultBotSession::class.java)
    }

    @Bean
    fun createTimer(): Timer {
        return Timer()
    }

}