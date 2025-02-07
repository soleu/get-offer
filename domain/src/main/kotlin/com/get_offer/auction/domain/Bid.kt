package com.get_offer.auction.domain

import com.get_offer.common.AuditingTimeEntity
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.math.BigDecimal

@Entity
@Table(name = "BIDS")
class Bid(
    val productId: Long,

    val bidderId: Long,

    val bidPrice: BigDecimal,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0L

) : AuditingTimeEntity() {
    init {
        validateBid()
    }

    private fun validateBid() {
        if (bidPrice <= BigDecimal.ZERO) {
            throw IllegalArgumentException("경매가는 0보다 커야합니다.")
        }

        val minBidPrice = BigDecimal("100")
        if (bidPrice < minBidPrice) {
            throw IllegalArgumentException("경매가는 최소 ${minBidPrice}원 이상이어야 합니다.")
        }
    }
}

