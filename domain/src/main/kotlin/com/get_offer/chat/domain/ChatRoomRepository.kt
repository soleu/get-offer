package com.get_offer.chat.domain

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ChatRoomRepository : JpaRepository<ChatRoom, Long> {
    fun findByRequesterIdAndProductId(buyerId: Long, itemId: Long): ChatRoom?
}