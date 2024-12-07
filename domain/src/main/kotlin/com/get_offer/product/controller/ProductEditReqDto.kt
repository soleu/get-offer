package com.get_offer.product.controller

import com.get_offer.product.domain.Category
import java.math.BigDecimal
import java.time.LocalDateTime

data class ProductEditReqDto(
    val title: String?,
    val category: Category?,
    val description: String?,
    val startPrice: BigDecimal?,
    val startDate: LocalDateTime?,
    val endDate: LocalDateTime?,
)