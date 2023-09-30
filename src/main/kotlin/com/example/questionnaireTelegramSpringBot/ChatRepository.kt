package com.example.questionnaireTelegramSpringBot

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ChatRepository : CrudRepository<ChatEntity, ChatId>