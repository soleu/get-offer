package com.get_offer.product.service

import com.get_offer.product.controller.ProductEditReqDto
import com.get_offer.product.domain.Category
import java.time.LocalDateTime
import org.springframework.web.multipart.MultipartFile

data class ProductEditDto(
    val productId: Long,
    val title: String?,
    val category: Category?,
    val description: String?,
    val startPrice: Int?,
    val startDate: LocalDateTime?,
    val endDate: LocalDateTime?,
    val writerId: Long,
    val images: List<MultipartFile>?
) {
    companion object {
        fun of(productId: Long, req: ProductEditReqDto, userId: Long, images: List<MultipartFile>?): ProductEditDto {
            return ProductEditDto(
                productId = productId,
                title = req.title,
                category = req.category,
                description = req.description,
                startPrice = req.startPrice,
                startDate = req.startDate,
                endDate = req.endDate,
                writerId = userId,
                images = images
            )
        }
    }
}