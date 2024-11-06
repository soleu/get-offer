package com.get_offer.auction.domain

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AuctionHistoryRepository : JpaRepository<Bid, Long>