package com.get_offer.product.service

import com.get_offer.product.domain.Category
import com.get_offer.product.domain.ProductStatus
import java.time.LocalDateTime

data class ProductListDto(
    val id: Long?,
    val writerId: Long,
    val name: String,
    val category: Category,
    val thumbNail: String,
    val currentPrice: Int,
    val status: ProductStatus,
    val startDate: LocalDateTime,
    val endDate: LocalDateTime,
    val isMine: Boolean,
)