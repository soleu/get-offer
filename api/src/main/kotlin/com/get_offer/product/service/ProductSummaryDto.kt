package com.get_offer.product.service

data class ProductSummaryDto(
    val productId: Long,
    val summaryText: String,
) {
    companion object {
        fun of(productId: Long, summaryText: String): ProductSummaryDto {
            return ProductSummaryDto(productId, summaryText)
        }
    }
}