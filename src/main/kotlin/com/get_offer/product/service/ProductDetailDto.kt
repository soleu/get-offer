package com.get_offer.product.service

import com.get_offer.product.domain.Category
import com.get_offer.product.domain.ProductStatus
import java.time.LocalDateTime

data class ProductDetailDto(
    val id: Long?,
    val writerId: Long,
    val writerNickname: String,
    val writerProfileImg: String,
    val name: String,
    val category: Category,
    val images: List<String>,
    val description: String,
    val startPrice: Int,
    val currentPrice: Int,
    val status: ProductStatus,
    val startDate: LocalDateTime,
    val endDate: LocalDateTime,
    val isMine: Boolean,
)