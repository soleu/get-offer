package com.get_offer.product.service

import com.get_offer.product.domain.ProductStatus
import com.get_offer.product.repository.ProductRepository
import org.springframework.stereotype.Service

@Service
class ProductService(
    private val productRepository: ProductRepository,
) {
    fun getActiveProductList(userId: Long): List<ProductListDto> {
        val productList =
            productRepository.findAllByStatusInOrderByEndDateDesc(listOf(ProductStatus.IN_PROGRESS, ProductStatus.WAIT))

        return productList.map { x ->
            val imageList = x.images.split(";")

            ProductListDto(
                writerId = x.writerId,
                name = x.name,
                category = x.category,
                thumbNail = imageList[0],
                images = imageList,
                description = x.description,
                currentPrice = x.currentPrice,
                status = x.status,
                startDate = x.startDate,
                endDate = x.endDate,
                isMine = x.writerId == userId,
            )
        }
    }
}