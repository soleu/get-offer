package com.get_offer.auction.domain

import com.get_offer.common.AuditingTimeEntity
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.math.BigDecimal
import org.apache.coyote.BadRequestException

@Entity
@Table(name = "BIDS")
class Bid(
    val productId: Long,

    val bidderId: Long,

    val biddingPrice: BigDecimal,

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L

) : AuditingTimeEntity() {
    init {
        validateBid()
    }

    private fun validateBid() {
        if (biddingPrice <= BigDecimal.ZERO) {
            throw BadRequestException("경매가는 0보다 커야합니다.")
        }

        val minBidPrice = BigDecimal("100")
        if (biddingPrice < minBidPrice) {
            throw BadRequestException("경매가는 최소 ${minBidPrice}원 이상이어야 합니다.")
        }
    }
}

