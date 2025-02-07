package com.get_offer.auction.service

import com.get_offer.auction.controller.BidRequest
import com.get_offer.auction.domain.AuctionResult
import com.get_offer.auction.domain.AuctionResultRepository
import com.get_offer.common.exception.ApiException
import com.get_offer.common.exception.ExceptionCode
import com.get_offer.common.redis.RedissonLock
import com.get_offer.product.domain.Product
import com.get_offer.product.domain.ProductRepository
import com.get_offer.user.domain.User
import com.get_offer.user.domain.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuctionService(
    private val productRepository: ProductRepository,
    private val auctionRepository: AuctionResultRepository,
    private val userRepository: UserRepository,
) {
    fun getSellHistory(userId: Long): List<SellAuctionDto> {
        val productList = productRepository.findAllByWriterIdOrderByEndDateDesc(userId)

        return productList.map { SellAuctionDto.of(it, userId) }
    }

    fun getBuyHistory(userId: Long): List<BuyAuctionDto> {
        // 옥션 최종 정보에서 buyer가 나인걸 찾음
        val auctionResults = auctionRepository.findAllByBuyerIdOrderByCreatedAtDesc(userId)
        // 해당 상품 정보를 찾음
        return auctionResults.map {
            val product = productRepository.findById(it.productId)
                .orElseThrow { ApiException(ExceptionCode.NOTFOUND, "${it.productId} 의 상품은 존재하지 않습니다.") }
            BuyAuctionDto.of(it, product)
        }
    }

    fun getSoldAuctionDetail(userId: Long, auctionId: Long): SellAuctionDetailDto {
        val (auction, product) = getAuctionAndProduct(auctionId)
        if (userId != product.writerId) throw ApiException(ExceptionCode.UN_AUTHORIZED)

        val buyer = getUser(auction.buyerId)

        return SellAuctionDetailDto.of(product, buyer, auction)
    }

    fun getBoughtAuctionDetail(userId: Long, auctionId: Long): BuyAuctionDetailDto {
        val (auction, product) = getAuctionAndProduct(auctionId)
        if (userId != auction.buyerId) throw ApiException(ExceptionCode.UN_AUTHORIZED)

        val seller = getUser(product.writerId)

        return BuyAuctionDetailDto.of(product, seller, auction)
    }

    // 분산락 사용방식
    @Transactional
    @RedissonLock(value = "#productId")
    fun bidAuction(userId: Long, productId: Long, bidRequest: BidRequest): Boolean? {
        val product = productRepository.findById(productId)
            .orElseThrow { ApiException(ExceptionCode.NOTFOUND, "$productId 의 경매 내역은 존재하지 않습니다.") }

        product.placeBid(bidRequest.bidPrice, userId)
        return true
    }

    // 비관적락 사용방식
//    @Transactional
//    fun bidAuction(userId: Long, productId: Long, bidRequest: BidRequest): Boolean {
//        val product = productRepository.findByIdWithLock(productId)
//            .orElseThrow { ApiException(ExceptionCode.NOTFOUND, "$productId 의 경매 내역은 존재하지 않습니다.") }
//
//        product.placeBid(bidRequest.bidPrice, userId)
//
//        bidRepository.save(
//            Bid(
//                productId = product.id,
//                bidderId = userId,
//                bidPrice = bidRequest.bidPrice,
//            )
//        )
//        return true
//    }

    private fun getAuctionAndProduct(auctionId: Long): Pair<AuctionResult, Product> {
        val auction = auctionRepository.findById(auctionId)
            .orElseThrow { ApiException(ExceptionCode.NOTFOUND, "$auctionId 의 경매 내역은 존재하지 않습니다.") }

        val product = productRepository.findById(auction.productId)
            .orElseThrow { ApiException(ExceptionCode.NOTFOUND, "${auction.productId} 의 상품은 존재하지 않습니다.") }

        return Pair(auction, product)
    }

    private fun getUser(userId: Long): User {
        return userRepository.findById(userId)
            .orElseThrow { ApiException(ExceptionCode.NOTFOUND, "$userId 의 사용자는 존재하지 않습니다.") }
    }
}