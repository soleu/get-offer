package com.get_offer

import com.get_offer.auction.domain.AuctionResult
import com.get_offer.auction.domain.AuctionStatus
import com.get_offer.product.domain.Category
import com.get_offer.product.domain.Product
import com.get_offer.product.domain.ProductImagesVo
import com.get_offer.product.domain.ProductStatus
import com.get_offer.user.domain.User
import java.math.BigDecimal
import java.time.LocalDateTime

object TestFixtures {
    fun createProductInProgress(userId: Long?): Product {
        return Product(
            userId ?: 1L,
            "product",
            Category.GAMES,
            ProductImagesVo(listOf("https://image1.png", "https://image2.png")),
            "desc for product",
            BigDecimal(10000),
            BigDecimal(15000),
            ProductStatus.IN_PROGRESS,
            LocalDateTime.now(),
            LocalDateTime.now()
        )
    }

    fun createProductWait(userId: Long?): Product {
        return Product(
            userId ?: 2L,
            "product2",
            Category.SPORTS,
            ProductImagesVo(listOf("https://image1.png", "https://image2.png")),
            "desc for product",
            BigDecimal(12000),
            BigDecimal(15500),
            ProductStatus.WAIT,
            LocalDateTime.now(),
            LocalDateTime.now()
        )
    }

    fun createProductCompleted(userId: Long?): Product {
        return Product(
            userId ?: 3L,
            "product3",
            Category.SPORTS,
            ProductImagesVo(listOf("https://image1.png", "https://image2.png")),
            "desc for product",
            BigDecimal(13000),
            BigDecimal(16500),
            ProductStatus.COMPLETED,
            LocalDateTime.now(),
            LocalDateTime.now()
        )
    }

    fun createAuction(buyerId: Long?): AuctionResult {
        return AuctionResult(
            productId = 1L,
            buyerId = buyerId ?: 1L,
            finalPrice = BigDecimal(15500),
            auctionStatus = AuctionStatus.COMPLETED,
            auctionName = "product1"
        )
    }

    fun createBuyer(buyerId: Long?): User {
        return User(
            nickname = "buyer1",
            image = "image.png",
            id = buyerId ?: 1L,
        )
    }


}