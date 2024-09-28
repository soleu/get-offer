package com.get_offer.product.service

import com.get_offer.product.domain.Category
import com.get_offer.product.domain.ProductStatus
import java.time.LocalDateTime

data class ActiveProductListDto(
    val writerId: Long,

    val name: String,

    val category: Category,

    val thumbNail: String,

    val images: List<String>,

    val description: String,

    val currentPrice: Int,

    val status: ProductStatus,

    val startDate: LocalDateTime,

    val endDate: LocalDateTime,

    val isMine: Boolean,
)