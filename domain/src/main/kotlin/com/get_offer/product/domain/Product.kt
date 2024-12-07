package com.get_offer.product.domain

import com.get_offer.common.AuditingTimeEntity
import jakarta.persistence.Column
import jakarta.persistence.Convert
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

@Entity
@Table(name = "PRODUCTS")
class Product(
    val writerId: Long,

    var title: String,

    @Enumerated(EnumType.STRING)
    var category: Category,

    @Convert(converter = ProductImagesConverter::class)
    @Column(name = "IMAGES")
    var images: ProductImagesVo,

    var description: String,

    var startPrice: BigDecimal,

    var currentPrice: BigDecimal,

    @Enumerated(EnumType.STRING)
    var status: ProductStatus,

    var startDate: LocalDateTime,

    var endDate: LocalDateTime,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0L,
) : AuditingTimeEntity() {
    init {
        validateProduct(startPrice, startDate, endDate)
    }

    fun placeBid(bidPrice: BigDecimal, userId: Long) {
        if (writerId == userId) {
            throw IllegalArgumentException("판매자가 경매를 할수는 없습니다.")
        }
        if (currentPrice >= bidPrice) {
            throw IllegalArgumentException("경매가가 경매 금액보다 낮거나 같을 수는 없습니다.")
        }

        if (status != ProductStatus.IN_PROGRESS) {
            throw IllegalArgumentException("진행중인 경매만 입찰을 할 수 있습니다.")
        }

        this.currentPrice = bidPrice
    }

    fun update(
        startDate: LocalDateTime?,
        endDate: LocalDateTime?,
        title: String?,
        category: Category?,
        startPrice: BigDecimal?,
        images: List<String>?
    ) {
        startDate?.let { this.startDate = it }
        endDate?.let { this.endDate = it }
        title?.let { this.title = it }
        category?.let { this.category = it }
        startPrice?.let { this.startPrice = it }
        images?.let { this.images = ProductImagesVo(it) }
    }


    private fun validateProduct(startPrice: BigDecimal, startDate: LocalDateTime, endDate: LocalDateTime) {
        validateStartPrice(startPrice)
        validateDateRange(startDate, endDate)
    }

    private fun validateStartPrice(startPrice: BigDecimal) {
        if (startPrice < BigDecimal.ZERO) {
            throw IllegalArgumentException("startPrice가 0보다 작을 수 없습니다.")
        }
    }

    private fun validateDateRange(startDate: LocalDateTime, endDate: LocalDateTime) {
        if (startDate.isAfter(endDate)) throw IllegalArgumentException("시작 날짜가 유효하지 않습니다.")
        if (ChronoUnit.DAYS.between(startDate, endDate) > 7) throw IllegalArgumentException("경매 기간은 7일을 넘길 수 없습니다.")
    }

    companion object {
        fun checkStatus(startDate: LocalDateTime): ProductStatus {
            if (LocalDateTime.now().isBefore(startDate)) {
                return ProductStatus.WAIT
            }
            return ProductStatus.IN_PROGRESS
        }
    }
}
