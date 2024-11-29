package com.get_offer.auction.domain

import java.util.*
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BidRepository : JpaRepository<Bid, Long> {
    fun findFirstByProductIdOrderByBidPriceDesc(productId: Long): Optional<Bid>
    fun countAllByProductId(productId: Long): Long
}