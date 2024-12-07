package com.get_offer.product.domain

data class ProductImagesVo(
    val images: List<String>
) {

    // for jackson
    private constructor() : this(emptyList())

    fun thumbnail() = images.firstOrNull() ?: "default.png"
}