package com.get_offer.auction_result.domain

import com.get_offer.common.AuditingTimeEntity
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class History(
    private val productId: Long,

    private val userId: Long,

    private val biddingPrice: Int,

    @Enumerated(EnumType.STRING)
    val auctionStatus: AuctionStatus,

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private val id: Long? = null

) : AuditingTimeEntity()