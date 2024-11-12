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
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import org.apache.coyote.BadRequestException

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

    var startPrice: Int,

    var currentPrice: Int,

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

    fun update(newProductReq: ProductEditReq) {
        newProductReq.startDate?.let { this.startDate = it }
        newProductReq.endDate?.let { this.endDate = it }
        newProductReq.title?.let { this.title = it }
        newProductReq.category?.let { this.category = it }
        newProductReq.startPrice?.let { this.startPrice = it }
        newProductReq.images?.let { this.images = ProductImagesVo(it) }
    }


    private fun validateProduct(startPrice: Int, startDate: LocalDateTime, endDate: LocalDateTime) {
        validateStartPrice(startPrice)
        validateDateRange(startDate, endDate)
    }

    private fun validateStartPrice(startPrice: Int) {
        if (startPrice < 0) {
            throw BadRequestException("startPrice가 0보다 작을 수 없습니다.")
        }
    }

    private fun validateDateRange(startDate: LocalDateTime, endDate: LocalDateTime) {
        if (startDate.isAfter(endDate)) throw BadRequestException("시작 날짜가 유효하지 않습니다.")
        if (ChronoUnit.DAYS.between(startDate, endDate) > 7) throw BadRequestException("경매 기간은 7일을 넘길 수 없습니다.")
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
