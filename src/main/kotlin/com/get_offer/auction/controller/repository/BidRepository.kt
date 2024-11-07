package com.get_offer.auction.controller.repository

import com.get_offer.auction.domain.Bid
import java.util.*
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BidRepository : JpaRepository<Bid, Long> {
    fun findFirstByProductIdOrderByBidPriceDesc(productId: Long): Optional<Bid>
}