package com.get_offer.auction.domain

import com.get_offer.common.AuditingTimeEntity
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "BIDS")
class Bid(
    val productId: Long,

    val bidderId: Long,

    val biddingPrice: Int,

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

) : AuditingTimeEntity()